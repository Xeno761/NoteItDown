package com.xeno761.noteitdown.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.SortByAlpha
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeno761.noteitdown.MainActivity
import com.xeno761.noteitdown.R
import com.xeno761.noteitdown.data.local.entity.FolderEntity
import com.xeno761.noteitdown.data.local.entity.NoteEntity
import com.xeno761.noteitdown.presentation.component.AdaptiveNavigationScreen
import com.xeno761.noteitdown.presentation.component.ColumnNoteCard
import com.xeno761.noteitdown.presentation.component.DrawerContent
import com.xeno761.noteitdown.presentation.component.dialog.ExportDialog
import com.xeno761.noteitdown.presentation.component.dialog.FolderListDialog
import com.xeno761.noteitdown.presentation.component.GridNoteCard
import com.xeno761.noteitdown.presentation.event.ListEvent
import com.xeno761.noteitdown.presentation.component.dialog.ProgressDialog
import com.xeno761.noteitdown.presentation.component.AdaptiveTopSearchbar
import com.xeno761.noteitdown.presentation.component.dialog.OrderSectionDialog
import com.xeno761.noteitdown.presentation.event.DatabaseEvent
import com.xeno761.noteitdown.presentation.navigation.Screen
import com.xeno761.noteitdown.presentation.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    sharedViewModel: SharedViewModel = hiltViewModel(LocalContext.current as MainActivity),
    isLargeScreen: Boolean,
    navigateToNote: (Long) -> Unit,
    navigateToScreen: (Screen) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    val dataState by sharedViewModel.dataStateFlow.collectAsStateWithLifecycle()
    val settingsState by sharedViewModel.settingsStateFlow.collectAsStateWithLifecycle()
    val folderList by sharedViewModel.foldersStateFlow.collectAsStateWithLifecycle()
    val dataActionState by sharedViewModel.dataActionStateFlow.collectAsStateWithLifecycle()

    val staggeredGridState = rememberLazyStaggeredGridState()
    val lazyListState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Search bar state, reset when configuration changes, no need to use rememberSaveable
    var isSearchBarActivated by remember { mutableStateOf(false) }

    // Selected drawer item and folder, 0 for all, 1 for trash, others for folder index
    var selectedDrawerIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedFolder by rememberSaveable(stateSaver = object :
        Saver<FolderEntity, Triple<Long?, String, Int?>> {
        override fun restore(value: Triple<Long?, String, Int?>): FolderEntity {
            return FolderEntity(value.first, value.second, value.third)
        }

        override fun SaverScope.save(value: FolderEntity): Triple<Long?, String, Int?> {
            return Triple(value.id, value.name, value.color)
        }
    }) { mutableStateOf(FolderEntity()) }

    // Record whether multi-select mode has been enabled, selected items and whether all items have been selected
    var isMultiSelectionModeEnabled by remember { mutableStateOf(false) }
    var selectedNotes by remember { mutableStateOf<Set<NoteEntity>>(emptySet()) }
    var allNotesSelected by remember { mutableStateOf(false) }

    // Whether to show the floating button, determined by the scroll state of the grid, the selected drawer, the search bar, and whether multi-select mode is enabled
    val isFloatingButtonVisible by remember {
        derivedStateOf {
            selectedDrawerIndex == 0 && !isSearchBarActivated && !isMultiSelectionModeEnabled
                    && !staggeredGridState.isScrollInProgress && !lazyListState.isScrollInProgress
        }
    }

    // Reset multi-select mode
    fun initializeNoteSelection() {
        isMultiSelectionModeEnabled = false
        selectedNotes = emptySet()
        allNotesSelected = false
    }

    // select all and deselect all, triggered by the checkbox in the bottom bar
    LaunchedEffect(allNotesSelected) {
        selectedNotes = if (allNotesSelected) selectedNotes.plus(dataState.notes)
        else selectedNotes.minus(dataState.notes.toSet())
    }

    // Back logic for better user experience
    BackHandler(isMultiSelectionModeEnabled) {
        if (isMultiSelectionModeEnabled) {
            initializeNoteSelection()
        }
    }

    // Bottom sheet visibility, reset when configuration changes, no need to use rememberSaveable
    var isFolderDialogVisible by remember {
        mutableStateOf(false)
    }

    // Whether to show the export dialog
    var isExportDialogVisible by remember {
        mutableStateOf(false)
    }

    AdaptiveNavigationScreen(
        isLargeScreen = isLargeScreen,
        drawerState = drawerState,
        gesturesEnabled = !isMultiSelectionModeEnabled && !isSearchBarActivated,
        drawerContent = {
            DrawerContent(
                folderList = folderList,
                selectedDrawerIndex = selectedDrawerIndex,
                navigateTo = { navigateToScreen(it) }
            ) { position, folderEntity ->
                if (selectedDrawerIndex != position) {
                    initializeNoteSelection()
                    selectedDrawerIndex = position
                    selectedFolder = folderEntity
                    when (position) {
                        0 -> sharedViewModel.onListEvent(ListEvent.Sort(trash = false))

                        1 -> sharedViewModel.onListEvent(ListEvent.Sort(trash = true))

                        else -> sharedViewModel.onListEvent(
                            ListEvent.Sort(
                                filterFolder = true,
                                folderId = folderEntity.id
                            )
                        )
                    }
                }
                if (!isLargeScreen)
                    coroutineScope.launch {
                        drawerState.apply {
                            close()
                        }
                    }
            }
        },
    ) {
        Scaffold(
            topBar = {
                AnimatedContent(targetState = selectedDrawerIndex == 0, label = "") {
                    if (it) {
                        AdaptiveTopSearchbar(
                            enabled = !isMultiSelectionModeEnabled,
                            isLargeScreen = isLargeScreen,
                            onSearchBarActivationChange = { activated ->
                                isSearchBarActivated = activated
                            },
                            onDrawerStateChange = {
                                coroutineScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    } else {

                        var showMenu by remember {
                            mutableStateOf(false)
                        }

                        TopAppBar(
                            title = {
                                Text(
                                    text = if (selectedDrawerIndex == 1) stringResource(id = R.string.trash)
                                    else selectedFolder.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                if (!isLargeScreen) {
                                    IconButton(
                                        enabled = !isMultiSelectionModeEnabled,
                                        onClick = {
                                            coroutineScope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Menu,
                                            contentDescription = "Open Menu"
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(onClick = { sharedViewModel.onListEvent(ListEvent.ChangeViewMode) }) {
                                    Icon(
                                        imageVector = if (!settingsState.isListView) Icons.Outlined.ViewAgenda else Icons.Outlined.GridView,
                                        contentDescription = "View Mode"
                                    )
                                }
                                IconButton(onClick = { sharedViewModel.onListEvent(ListEvent.ToggleOrderSection) }) {
                                    Icon(
                                        imageVector = Icons.Outlined.SortByAlpha,
                                        contentDescription = "Sort"
                                    )
                                }
                                if (selectedDrawerIndex == 1) {
                                    IconButton(onClick = { showMenu = !showMenu }) {
                                        Icon(
                                            imageVector = Icons.Outlined.MoreVert,
                                            contentDescription = "More"
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false }) {
                                        DropdownMenuItem(
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Outlined.RestartAlt,
                                                    contentDescription = "Restore"
                                                )
                                            },
                                            text = { Text(text = stringResource(id = R.string.restore_all)) },
                                            onClick = {
                                                sharedViewModel.onListEvent(
                                                    ListEvent.RestoreNotes(dataState.notes)
                                                )
                                            })

                                        DropdownMenuItem(
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Outlined.Delete,
                                                    contentDescription = "Delete"
                                                )
                                            },
                                            text = { Text(text = stringResource(id = R.string.delete_all)) },
                                            onClick = {
                                                sharedViewModel.onListEvent(
                                                    ListEvent.DeleteNotes(dataState.notes, false)
                                                )
                                            })
                                    }
                                }
                            }
                        )
                    }
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = isMultiSelectionModeEnabled,
                    enter = slideInVertically { fullHeight -> fullHeight },
                    exit = slideOutVertically { fullHeight -> fullHeight }
                ) {
                    BottomAppBar {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Checkbox(
                                    checked = allNotesSelected,
                                    onCheckedChange = { allNotesSelected = it }
                                )

                                Text(text = stringResource(R.string.checked))

                                Text(text = selectedNotes.size.toString())
                            }

                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                if (selectedDrawerIndex == 1) {
                                    TextButton(onClick = {
                                        sharedViewModel.onListEvent(
                                            ListEvent.RestoreNotes(selectedNotes)
                                        )
                                        initializeNoteSelection()
                                    }) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Outlined.RestartAlt,
                                                contentDescription = "Restore"
                                            )
                                            Text(text = stringResource(id = R.string.restore))
                                        }
                                    }
                                } else {
                                    TextButton(onClick = {
                                        isExportDialogVisible = true
                                    }) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Outlined.Upload,
                                                contentDescription = "Export"
                                            )
                                            Text(text = stringResource(id = R.string.export))
                                        }
                                    }

                                    TextButton(onClick = { isFolderDialogVisible = true }) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.DriveFileMove,
                                                contentDescription = "Move"
                                            )
                                            Text(text = stringResource(id = R.string.move))
                                        }
                                    }
                                }

                                TextButton(onClick = {
                                    sharedViewModel.onListEvent(
                                        ListEvent.DeleteNotes(
                                            selectedNotes,
                                            selectedDrawerIndex != 1
                                        )
                                    )
                                    initializeNoteSelection()
                                }) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Delete"
                                        )
                                        Text(text = stringResource(id = R.string.delete))
                                    }
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isFloatingButtonVisible,
                    enter = slideInHorizontally { fullWidth -> fullWidth * 3 / 2 },
                    exit = slideOutHorizontally { fullWidth -> fullWidth * 3 / 2 }) {
                    FloatingActionButton(
                        onClick = {
                            sharedViewModel.onListEvent(ListEvent.AddNote)
                            navigateToNote(-1)
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
                    }
                }

            }) { innerPadding ->

            val layoutDirection = LocalLayoutDirection.current
            val displayCutout = WindowInsets.displayCutout.asPaddingValues()
            val paddingValues = remember(layoutDirection, displayCutout) {
                PaddingValues(
                    top = 72.dp,
                    start = displayCutout.calculateStartPadding(layoutDirection),
                    end = displayCutout.calculateEndPadding(layoutDirection)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(paddingValues)
            ) {

                if (dataState.notes.isEmpty()) {
                    return@Box
                }

                if (!settingsState.isListView) {
                    LazyVerticalStaggeredGrid(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = staggeredGridState,
                        // The staggered grid layout is adaptive, with a minimum column width of 160dp(mdpi)
                        columns = StaggeredGridCells.Adaptive(160.dp),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        // for better edgeToEdge experience
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = innerPadding.calculateBottomPadding()
                        ),
                        content = {
                            items(
                                dataState.notes,
                                key = { item: NoteEntity -> item.id!! }) { note ->
                                GridNoteCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem(), // Add animation to the item
                                    note = note,
                                    isEnabled = isMultiSelectionModeEnabled,
                                    isSelected = selectedNotes.contains(note),
                                    onEnableChange = { isMultiSelectionModeEnabled = it },
                                    onNoteClick = {
                                        if (isMultiSelectionModeEnabled) {
                                            selectedNotes =
                                                if (selectedNotes.contains(it)) selectedNotes.minus(
                                                    it
                                                )
                                                else selectedNotes.plus(it)
                                        } else {
                                            if (selectedDrawerIndex != 1) {
                                                sharedViewModel.onListEvent(ListEvent.Noteitdown(it))
                                                navigateToNote(it.id!!)
                                            } else {
                                                Unit
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    )
                } else {

                    VerticalDivider(
                        Modifier
                            .align(Alignment.TopStart)
                            .fillMaxHeight()
                            .padding(start = 15.dp),
                        thickness = 2.dp
                    )

                    LazyColumn(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize(),
                        state = lazyListState,
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            end = 16.dp,
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    ) {
                        items(
                            dataState.notes,
                            key = { item: NoteEntity -> item.id!! }) { note ->
                            ColumnNoteCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(), // Add animation to the item
                                note = note,
                                isEnabled = isMultiSelectionModeEnabled,
                                isSelected = selectedNotes.contains(note),
                                onEnableChange = { isMultiSelectionModeEnabled = it },
                                onNoteClick = {
                                    if (isMultiSelectionModeEnabled) {
                                        selectedNotes =
                                            if (selectedNotes.contains(it)) selectedNotes.minus(
                                                it
                                            )
                                            else selectedNotes.plus(it)
                                    } else {
                                        if (selectedDrawerIndex != 1) {
                                            sharedViewModel.onListEvent(ListEvent.Noteitdown(it))
                                            navigateToNote(it.id!!)
                                        } else {
                                            Unit
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            if (dataState.isOrderSectionVisible) {
                OrderSectionDialog(
                    noteOrder = dataState.noteOrder,
                    onOrderChange = {
                        sharedViewModel.onListEvent(
                            ListEvent.Sort(
                                noteOrder = it,
                                trash = selectedDrawerIndex == 1,
                                filterFolder = selectedDrawerIndex != 0 && selectedDrawerIndex != 1,
                                folderId = selectedFolder.id
                            )
                        )
                    },
                    onDismiss = { sharedViewModel.onListEvent(ListEvent.ToggleOrderSection) }
                )
            }

            if (isExportDialogVisible) {
                val context = LocalContext.current
                ExportDialog(onDismissRequest = { isExportDialogVisible = false }) {
                    sharedViewModel.onDatabaseEvent(
                        DatabaseEvent.Export(
                            context.contentResolver,
                            selectedNotes.toList(),
                            it
                        )
                    )
                    isExportDialogVisible = false
                }
            }

            if (isFolderDialogVisible) {
                FolderListDialog(
                    hint = stringResource(R.string.destination_folder),
                    oFolderId = selectedFolder.id,
                    folders = folderList,
                    onDismissRequest = { isFolderDialogVisible = false }
                ) {
                    sharedViewModel.onListEvent(ListEvent.MoveNotes(selectedNotes, it))
                    initializeNoteSelection()
                }
            }

            ProgressDialog(
                isLoading = dataActionState.loading,
                progress = dataActionState.progress,
                onDismissRequest = sharedViewModel::cancelDataAction
            )
        }
    }
}
