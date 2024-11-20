![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/static/v1?style=for-the-badge&message=Jetpack+Compose&color=4285F4&logo=Jetpack+Compose&logoColor=FFFFFF&label=)
![LaTeX](https://img.shields.io/badge/latex-%23008080.svg?style=for-the-badge&logo=latex&logoColor=white)
![Markdown](https://img.shields.io/badge/markdown-%23000000.svg?style=for-the-badge&logo=markdown&logoColor=white)

NoteitDown is a modern Android note-taking application.  
It is developed using Kotlin (Compose) and follows the MVVM (Model-View-ViewModel) architecture
pattern along with Clean Architecture principles.

## Features

- **Create, Edit, and Delete Notes**: Users can create, edit, and delete notes effortlessly.
- **Create, Edit, and Delete Folders**: Organize notes efficiently with folder management
  functionalities.
- **Sorting and Filtering**: Easily sort and filter notes and folders based on various criteria.
- **Move Notes**: Seamlessly move notes between different folders for better organization.
- **Trash Bin**: Safely move notes to the trash for temporary storage before permanent deletion.
- **OCR Text Recognition**: Utilizes ML Kit and CameraX for optical character recognition (OCR)
  directly from images.
- **Markdown Support**: Supports both CommonMark and GitHub Flavored Markdown (GFM) syntax for
  versatile formatting options.
- **LaTeX Math Support**: Supports LaTeX math syntax for mathematical equations.
- **Mermaid Diagram Support**: Supports Mermaid syntax for creating diagrams and flowcharts.
- **Rich Text Mode**: Offers a simplified writing experience with basic rich text editing
  capabilities.
- **Export Options**: Notes can be exported in various formats including TXT, MD (Markdown), and
  HTML for versatile sharing and usage.
- **Material 3 Design**: Adheres to Material Design guidelines for a modern and cohesive user
  interface.
- **Responsive Design**: Optimized for devices with different screen sizes and orientations.

## Technical Details

- **Programming Languages**: Kotlin
- **Build Tool**: Gradle with Kotlin DSL
- **Android Version**: The application targets Android SDK version 34 and is compatible with devices
  running Android SDK version 29 and above.
- **Kotlin Version**: The application uses Kotlin version 2.0.0.
- **Java Version**: The application uses Java version 17.

## Architecture

- **MVVM (Model-View-ViewModel)**: Separates the user interface logic from the business logic,
  providing a clear separation of concerns.
- **Clean Architecture**: Emphasizes separation of concerns and layers of abstraction, making the
  application more modular, scalable, and maintainable.

## Libraries and Frameworks

- **Compose**: A modern toolkit for building native Android UI.
- **Hilt**: A dependency injection library for Android.
- **KSP (Kotlin Symbol Processing API)**: Enhances Kotlin compilation with additional metadata
  processing.
- **Room**: A persistence library providing an abstraction layer over SQLite.
- **Compose Navigation**: Simplifies the implementation of navigation between screens.
- **Material Icons**: Provides Material Design icons for consistent visual elements.
- **ML Kit**: Utilized for OCR text recognition.
- **CameraX**: Used for custom camera functionality.

## References

- [MaskAnim](https://github.com/setruth/MaskAnim): Implementation of the theme switching function
  using mask animation.
- [Notie](https://github.com/cworld1/notie): For reference and base designs
