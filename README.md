# Automation Base Framework

This repository now provides a lightweight Playwright + Cucumber + TestNG base for rapid UI automation.

## Tech Stack
- Java 21
- Playwright for Java
- Cucumber 7 + TestNG
- AssertJ Soft Assertions
- Allure Reporting
- Spotless formatting

## Getting Started
```powershell
# Install Playwright browsers
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# Run sample test
mvn clean test

# Serve Allure report
mvn allure:serve
```

## Structure
```
src/test/java/com/astrella/qa/
 base/ # BasePage
 context/ # TestContext lifecycle container
 runners/ # Cucumber TestNG runner
 steps/ # Hooks + sample steps
 pages/ # Sample page object(s)
 utils/ # Browser, Config, Factory, Assertions, Retry infra
src/test/resources/features/base/ # Sample feature files
src/test/resources/qa.properties # Base configuration
```

## Configuration (qa.properties)
Edit `src/test/resources/qa.properties`:
```
BASE_URL=about:blank
HEADLESS=true
BROWSER_NAME=chromium
DESKTOP_BROWSER=false
DEFAULT_TIMEOUT=30000
ENABLE_RETRY=false
RUNMODE=new
USERNAME=sample_user
PASSWORD=sample_password
```
Add new environment by duplicating the file (e.g., `staging.properties`) and enhancing `ConfigReader` to select by `-Denv=staging` if needed.

## Environment Selection
Run tests against different environments by passing -Denv. It will look for `src/test/resources/<env>.properties` and fallback to `qa.properties` if not found.

```powershell
mvn clean test -Denv=staging
mvn clean test -Denv=qa
```

## Tags
Use minimal taxonomy:
- @Smoke (quick feedback)
- @Regression (full suite)
- @UI (UI tests)

Adjust runner `tags` attribute for selection.

## Extending
1. Create new page in `pages/yourdomain` extending `BasePage`.
2. Add step definitions under `steps/yourdomain` injecting `TestContext`.
3. Add feature files under `features/<domain>`.
4. Use `testContext.getSoftly()` for soft assertions.

## Optional Components
- CSV data loading: `CsvDataManager` included but not wired; integrate if needed.
- Retry: enable by setting `ENABLE_RETRY=true` in properties.
- Trace & screenshots auto-attached on failures.

## Allure Categories
We include a minimal `src/test/resources/categories.json` to group common failures into Product/Test defects and mark likely flaky timeouts. You can customize this to fit your project conventions.

## CI
A minimal GitHub Actions workflow at `.github/workflows/ci.yml` runs `mvn clean test` on Java 21, installs Playwright browsers, and uploads `target/allure-results` as an artifact. Customize environment with `-Denv=<name>`.

## Parallel & Formatting
- Parallel: control threads centrally via TestNG and DataProvider; start with 2–4 threads for UI stability.
- Formatting: run `mvn spotless:apply` locally or add `spotless:check` to CI to enforce style.

## Next Steps
- Add environment selection logic once multiple env properties exist.
- Introduce API test layer if required.
- Implement advanced reporting categories in Allure.

Happy Testing!

