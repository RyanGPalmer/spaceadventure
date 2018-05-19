Use the following instructions to get the project up and running in IntelliJ on a new system.

# Prerequisites

* Git
* Latest version of IntelliJ
* JDK 9.0.4
* Gradle 4.7

# Set Up IntelliJ Project

1. Open IntelliJ
2. Select *Check out from Version Control*
3. Select *Git* from the drop-down menu
4. Enter the URL of the repo: *https://github.com/RyanGPalmer/spaceadventure*
5. Choose an installation directory (Recommended: ~/IdeaProjects/SpaceAdventure)
6. Click *Clone* and enter GitHub username and password if prompted
7. Click *Yes* when it asks to create a project from the sources
8. Choose *Import project from external model* and select *Gradle*
9. Click *Next*
10. Check *Use auto-import*
11. For *Gradle home:* enter the path of your Gradle installation
12. For *Gradle JVM:* choose *Use Project JDK*
13. For *Project format:* choose *.ipr (file based)*
14. Click *Finish*
15. If you receive a *Gradle projects need to be imported* message, click *Import Changes*
16. Allow Gradle to refresh the project.
17. Go to *File > Project Structure*
18. Under *Platform Settings > SDKs* click the ***+*** button and select *JDK* from the drop-down
19. Browse to your JDK 9.0.4 folder *(Ex: /Library/Java/jdk-9.0.4/)*
20. Click *OK*
21. Under *Project Settings > Project* set the *Project SDK* to ***9***
22. Set the *Project language level* to ***9***
23. Click *Apply* and *OK*

# Set Up IntelliJ Settings

1. Ask Ryan for the IntelliJ settings token
2. Go to *File > Settings Repository*
3. In *Upstream URL* enter this: https://github.com/RyanGPalmer/intellij-settings
4. Click *Overwrite Local*
5. Enter the token when prompted
6. IntelliJ will sync your settings

**Note:** Do NOT overwrite remote settings. The settings will sync automatically.

# Set Up Build Configurations

1. Go to *Run > Edit Configurations*
2. Click the ***+*** button and choose *JUnit*
3. For *Name:* enter *Tests*
4. For *Use classpath of module:* 
