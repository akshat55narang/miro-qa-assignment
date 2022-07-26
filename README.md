# Miro QA Assignment

## Goal:
- Verify that the sticker which was created on a board by the
  first user will appear on the board for the second user.

## How to use:
### download this project on your local machine
> git clone https://github.com/akshat55narang/miro-qa-assignment.git

### Tools Used
1. Programming Language - Java
2. Build Tool - Maven
3. Test Framework - TestNG
4. Web and UI automation tools - Selenium Webriver, RestAssured
5. Screenshot comparison - https://github.com/pazone/ashot
6. Reports - Surefire reports

### Approach
- Environment Variables - Variables such as URL are accessed via Properties file. See [ConfigManager](src/main/java/com/miro/test/configs/ConfigManager.java)
- Data Preparation - Create a Board via API ( only if it does not already exist). See [BoardApi](src/main/java/com/miro/test/api/BoardApi.java)
- Page Object Model design pattern used for re-usability. [pages](src/main/java/com/miro/test/pages)
- BasePage and BaseApi include all wrapper methods around WebDriver and RestAssured respectively
and also promote re-usability. [BasePage](src/main/java/com/miro/test/pages/BasePage.java) and [BaseApi](src/main/java/com/miro/test/api/BaseApi.java)
- Extra Helper functions to provide Looped Assertions. [Helpers](src/main/java/com/miro/test/utils/Helpers.java)
- [Base](src/test/java/com.miro.test/Base.java) class is generic class to externalize setup and tear down from each test class.

### How To Run
From the api-test-automation-kotlin directory
- Default configuration - `mvn clean test`
- Override default configuration - `mvn clean test -DbaseUrl=https://miro.com -DisBrowserShown=true -DdefaultTeamId=3458764529765127056`

Default configuration is located in [default.properties](default.properties)
The default values can be overridden from the command line, for example,
getBaseUrl method present in [ConfigManager](src/main/java/com/miro/test/configs/ConfigManager.java)
will return the value from [default.properties](default.properties) unless the value is supplied
via command line - `-DbaseUrl`

> public static String getBaseUrl() {
return getParameter("baseUrl", getConfig(BASE_URL));
}


### Test Artifact Location
Test Class - com.miro.test/StickerCreationTest.java
Failure Screen shots - target/screenshots
Report - target/surefire-reports/index.html

