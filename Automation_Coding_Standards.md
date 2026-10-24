# Automation Base Framework Coding Standards

(Former file name: `Automation_Coding_Standards.md` – renamed to remove legacy reference.)

## 1. Purpose & Scope
Defines engineering, coding, and test design standards for the base end‑to‑end UI automation framework (Java + Playwright + Cucumber + TestNG + Allure).

Goals:
- Readable, maintainable, extensible automation foundation
- Deterministic, parallel-safe execution
- Minimal flakiness & rich failure diagnostics (trace, screenshot, video)
- Clear patterns for rapid onboarding

Applies to: all framework code, future feature files, configuration, and execution infrastructure.

---
## 2. Core Architecture Principles
| Principle | Description | Enforced By |
|-----------|-------------|-------------|
| Single Responsibility | Each class (Page, Factory, Step, Utility) has one intent. | Code review |
| Encapsulation | Selectors & mechanics stay inside Page objects. | Page design |
| Declarative Gherkin | Scenarios express business intent only. | Feature review |
| Isolation | Each scenario gets its own BrowserContext. | Hooks |
| Externalized Config | Environment & runtime options in properties files. | ConfigReader |
| Fail Fast | Missing config keys throw early. | ConfigReader |
| Rich Diagnostics | Trace + screenshot (failure) captured automatically. | Hooks |
| Minimal Retry | Retries disabled by default; enabled intentionally. | Properties |

---
## 3. Repository Layout (Current Minimal Form)
```
src/test/java/com/framework/qa/
 base/ # BasePage abstraction
 context/ # Scenario-scoped TestContext
 pages/ # Page Objects (sample only now)
 runners/ # Cucumber/TestNG runner(s)
 steps/ # Hooks + Step definitions
 utils/ # BrowserFactory, ConfigReader, PageFactory, Assertions, Retry infra
src/test/resources/
 features/base/ # Sample feature(s)
 qa.properties # Primary environment config (extend with new env files later)
log4j2.xml # Console logging config
run-sample.ps1 # Helper script for local execution
pom.xml # Build & dependency management
```
Future optional folders (add when needed):
- `features/<domain>/` for new product areas
- `data/<env>/` for CSV test data (when data-driven layer reintroduced)
- `playwright/.auth/` for persisted auth states (if auth caching added)

---
## 4. Naming Conventions
| Item | Convention | Example                                      |
|------|------------|----------------------------------------------|
| Package | lowercase dot path | `com.framework.qa.pages.sample`              |
| Page Object | PascalCase + Page | `LoginPage`, `PortfolioPage`                 |
| Component (future) | PascalCase + Component | `NavigationBarComponent`                     |
| Modal (future) | PascalCase + Modal | `ConfirmActionModal`                         |
| Factory | PascalCase + Factory | `PageFactory`, `BrowserFactory`              |
| Step Class | <Domain><Action>Steps or <Domain>Steps | `SampleSteps`, `LoginSteps`                  |
| Feature File | kebab-case | `login.feature`, `portfolio-summary.feature` |
| Tags | PascalCase (short, semantic) | `@Smoke`, `@Regression`, `@UI`               |
| Env Property Keys | UPPER_SNAKE | `BASE_URL`, `HEADLESS`                       |
| Constants | UPPER_SNAKE static final | `DEFAULT_TIMEOUT_MS`                         |

---
## 5. Page Object Standards
1. Encapsulate selectors & user intent actions only.
2. Avoid assertions beyond simple presence checks—use custom assertion helpers for content/business validation.
3. Prefer semantic selectors (roles, labels) then stable CSS; avoid XPath unless necessary.
4. Make locators `private final`.
5. Provide high‑level methods (`login`, `openSettings`) instead of exposing low‑level clicks externally.
6. Rely on Playwright auto-waits; explicit waits only for spinner disappearance / non-auto states.
7. No static mutable state.

---
## 6. PageFactory Usage
- Retrieve pages via `testContext.getClassPage(MyPage.class)`.
- Constructor signature: `(Page page, TestContext testContext)`.
- Factory caches per scenario; do not manually cache pages elsewhere.
- Reinitialize PageFactory if browser context is recreated.

---
## 7. Hooks Lifecycle (Current)
`@Before`:
- Create Playwright instance (thread-local) & Browser via `BrowserFactory`.
- Create new BrowserContext (maximized viewport) & Page.
- Start tracing (snapshots, screenshots, sources).
- Initialize `SoftAssertions` + `PageFactory`.

`@After`:
- Stop tracing (zip saved in `traces/`).
- On failure: attach screenshot + trace.
- Assert all soft assertions.
- Close Page, Context, Browser, Playwright.

Optional future additions:
- CSV test data load step (before runtime setup).
- Auth state bootstrap & persistence.

---
## 8. Step Definitions
DO:
- Express business intent ("I open the sample page").
- Delegate all UI interactions to Pages.
- Keep short and cohesive.

AVOID:
- Direct selector usage.
- Embedding conditional logic—split into separate steps.
- Storing arbitrary scenario state outside `TestContext`.

---
## 9. Feature File Conventions
Minimal tag taxonomy (base):
- `@Smoke` quick feedback set
- `@Regression` full suite
- `@UI` UI layer classification
- Optional future: `@E2E`, `@API`, `@WIP`, `@Flaky`

Guidelines:
- Descriptive `Feature:` and concise scenarios (≤ 10 steps typical).
- Avoid `Background` until repeating setup is truly universal.
- Prefer one primary assertion outcome per scenario.

CSV Data (future):
- Reintroduce `@dataFile=<file.csv>` + `@testCase=<ID>` when data-driven flows are required.

---
## 10. Configuration Management
- Single base file: `qa.properties`. Additional env files: `<env>.properties` (e.g., `staging.properties`).
- Load via `-Denv=<env>`; fallback to `qa` if file absent.
- Fail fast on missing required property keys.

Sample keys:
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
---
## 11. Retry Policy
- Disabled by default (`ENABLE_RETRY=false`).
- Enable cautiously for temporary stabilization.
- Future enhancement: tag-scoped retry (e.g., only `@Flaky`).

---
## 12. Assertions & Validation
| Use Case | Mechanism | Notes |
|----------|-----------|-------|
| Element presence | `CustomAssertions.assertVisible(locator)` | Fast presence checks |
| Business validation | SoftAssertions (AssertJ) | Aggregate & flush at teardown |
| Text/content | Custom assertion helpers | Avoid brittle styling checks |

Use soft assertions to collect multiple related validations; fail once at scenario end.

---
## 13. Logging & Diagnostics
- Console logging via Log4j2 (simple pattern layout).
- Primary diagnostics: Playwright trace (always saved) & screenshot on failure.
- Video retention currently failure-only (can be enabled if needed).
- Avoid logging sensitive credentials—log user keys or roles only (once implemented).

---
## 14. Parallel Execution & Isolation
| Layer | Isolation Mechanism |
|-------|---------------------|
| Playwright | Thread-local instance |
| BrowserContext | New per scenario |
| PageFactory | Scenario-scoped |
| SoftAssertions | Scenario-scoped |

No shared mutable cross-thread state.

---
## 15. Performance & Stability
- No `Thread.sleep`; rely on Playwright auto-waits and targeted condition waits.
- Keep scenarios lean—UI navigation + key validation.
- If flakiness arises, capture trace & inspect rather than adding arbitrary waits.

---
## 16. Future Modules (Optional)
| Module | Purpose |
|--------|---------|
| AuthManager | Persist & reuse authenticated states |
| CSV Data Layer | Data-driven multi-row scenarios |
| DB Utilities | Backend data validation / setup |
| API Clients | Hybrid preconditioning |
| Network Mocking | Stability under backend volatility |
| Visual/Accessibility | Broader quality signals |

Introduce only when clear ROI; keep base minimal.

---
## 17. Build & Tooling
- Managed by `pom.xml` (Maven 3.9+, Java 21).
- Formatting via Spotless (Google Java Format).
- Allure reporting via plugin + Cucumber JVM integration.

Common commands:
```powershell
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install" # Install browsers
mvn clean test # Run all tests
mvn clean test -Denv=qa -Dcucumber.filter.tags=@Smoke # Run smoke subset
mvn allure:serve # View Allure report
```

---
## 18. Git & Branching (Recommended)
| Aspect | Standard |
|--------|----------|
| Branch Naming | `feature/<id>-short-desc`, `fix/<id>` |
| Commit Prefix | `feat:`, `fix:`, `test:`, `docs:`, `refactor:` |
| PR Content | Motivation, changes, evidence (trace/screenshot), risk |
| Size | Prefer ≤ 400 LOC net |

---
## 19. Quality Gates Targets (Initial)
| Metric | Target |
|--------|--------|
| Pass Rate | ≥ 95% (small set) |
| Flaky Rate | < 2% |
| Median Scenario Duration | < 30s |
| Untagged Scenarios | 0 |
| Missing Properties | 0 |

---
## 20. Anti‑Patterns (Avoid)
| Anti-Pattern | Why | Fix |
|--------------|-----|-----|
| Selectors in Steps | Breaks encapsulation | Move to Page |
| Static sleeps | Flaky / slow | Use auto-wait or explicit condition |
| Large step classes | Hard to read | Split by intent/domain |
| Hidden retries | Masks instability | Disable & fix root cause |
| Hardcoded secrets | Security risk | Externalize & secure vault integration |

---
## 21. Tag Reference (Current Minimal Set)
| Tag | Purpose |
|-----|---------|
| @Smoke | Fast confidence set |
| @Regression | Broader suite |
| @UI | UI layer classification |
| @WIP | Work in progress (excluded from CI) |
| @Flaky | Temporary stabilization target |

Usage examples:
```powershell
mvn clean test -Dcucumber.filter.tags=@Smoke
mvn clean test -Dcucumber.filter.tags="@Regression and not @Flaky"
```
Extend this taxonomy only when new dimensions (e.g., @API) are introduced.

---
## 22. Onboarding Quick Start
1. Install Java 21, Maven, Allure CLI.
2. Install Playwright browsers (`mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"`).
3. Run sample test: `mvn clean test`.
4. View report: `mvn allure:serve`.
5. Add a new feature under `features/<domain>/` and a matching Page + Step class.
6. Keep to standards above; open PR.

---
## 23. Future Enhancements Backlog
| Item | Benefit |
|------|---------|
| Environment metadata in Allure | Easier triage |
| Tag-based retry transformer | Controlled flakiness handling |
| Auth/session module | Faster secure flows |
| API preconditioning | Reduce UI setup time |
| Accessibility smoke | Inclusive quality |
| Network stubbing layer | Stability under outages |

---
## 24. Change Management
| Change Type | Process |
|-------------|---------|
| Minor (doc, small refactor) | PR + review |
| Structural (new module) | Design note / ADR + approval |
| Policy (retry, tagging) | Team consensus + version bump |

---
## 25. Quick Reference Cheat Sheet
- Use PageFactory: `testContext.getClassPage(MyPage.class)`
- Tags: `@Smoke`, `@Regression`, `@UI`
- Config: `-Denv=<env>` fallback to qa
- Assertions: SoftAssertions inside Pages or Steps, flush in teardown
- Diagnostics: Trace always; screenshot on failure
- No sleeps: rely on Playwright auto-wait
- Keep classes focused; one intent per class

---
*This document is the authoritative guide for contributing to the automation base. Update responsibly and keep the framework lean.*



Maximize compose field
Attach an image to your conversation with Samvel R
Attach a file to your conversation with Samvel R
Open GIF Keyboard
Open Emoji Keyboard
