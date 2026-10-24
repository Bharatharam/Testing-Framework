Param(
 [string]$Env = "qa",
 [string]$Tags = "@Smoke"
)
Write-Host "Running sample tests (env=$Env, tags=$Tags)" -ForegroundColor Cyan
mvn clean test -Denv=$Env -Dcucumber.filter.tags=$Tags
Write-Host "Done. To view Allure report run: mvn allure:serve" -ForegroundColor Green
