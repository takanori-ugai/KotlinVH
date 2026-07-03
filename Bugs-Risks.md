# Bugs and Risks Review for `./src`

This file now lists only findings that are still supported by the current sources.

## Additional Bug and Security Findings (`./src`)

### 1) Low (Contextual Security): Plain HTTP transport by default
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:50`
- **Issue:** URL is hardcoded as `http://$host:$port`.
- **Impact:** Usually acceptable for local VirtualHome setups, but vulnerable to interception/tampering on untrusted networks.
- **Recommendation:** Keep compatibility, but document trust assumptions and optionally support configurable `http`/`https`.
