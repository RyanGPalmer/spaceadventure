Use the following instructions to get the project up and running in IntelliJ on a new system.

# Prerequisites

* Git
* Latest version of IntelliJ
* JDK 9.0.4
* Gradle 4.7

# Set Up IntelliJ Settings

1. Ask Ryan for the IntelliJ settings token
2. Go to *File > Settings Repository*
3. In *Upstream URL* enter this: https://github.com/RyanGPalmer/intellij-settings
4. Click *Overwrite Local*
5. Enter the token when prompted
6. IntelliJ will sync your settings

**NOTE:** Do NOT overwrite remote settings. The settings will sync automatically.

# Set Up IntelliJ Project

1. Open terminal/Git bash
2. Navigate to your desired project directory *(Ex: ~/IdeaProjects/)*
3. Clone the repo: `git clone https://github.com/RyanGPalmer/spaceadventure`
4. Open the *.ipr* file with IntelliJ
5. Go to *File > Project Structure* and confirm the selected JDK is ***9***

**MAC/LINUX USERS:** You must go *Preferences > Build, Execution, Deployment > Build Tools > Gradle* and specify the correct *Gradle Home* path. *(Usually /usr/local/opt/gradle/libexec)*

# Building and Running the Project

1. **Optional Clean Build:** Go to *Build > Rebuild Project*, then open the *Gradle* panel and click the blue refresh button
2. To launch from *Windows* or *Linux*, run the ***Main*** configuration
3. To launch from *Mac OS*, run the ***Main (Mac)*** configuration

# Complete Project Refresh (Panic Mode)

1. Clean run configs (Necessary due to bug): `rm -r -f .runConfigurations/`
2. Reset the local repo: `git reset --hard`
3. Remove untracked/ignored files: `git clean -x -f`
4. Pull and rebase: `git pull --rebase origin master`
5. Re-open the project
