# Bugs and Risks Review for `./src`

This file now lists only findings that are still supported by the current sources.

## Additional Bug and Security Findings (`./src`)

### 1) Low (Contextual Security): Plain HTTP transport by default
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:50`
- **Issue:** URL is hardcoded as `http://$host:$port`.
- **Impact:** Usually acceptable for local VirtualHome setups, but vulnerable to interception/tampering on untrusted networks.
- **Recommendation:** Keep compatibility, but document trust assumptions and optionally support configurable `http`/`https`.

## Incremental Findings From `src/jsMain`

### 1) Medium: JS wrapper relies on manual `close()` with no lifecycle guard
- **Location:** `src/jsMain/kotlin/io/github/ugaikit/vh/JsVirtualHomeClient.kt:13`, `:29`
- **Issue:** `JsVirtualHomeClient` owns a `VirtualHomeClient` delegate and only exposes manual `close()`.
- **Impact:** In long-running Node.js processes, forgetting to call `close()` can keep HTTP resources alive longer than intended.
- **Recommendation:** Document mandatory close usage clearly and consider providing safer usage patterns/helpers that enforce cleanup.

### 2) Low/Medium: `timeout` constructor input is not validated
- **Location:** `src/jsMain/kotlin/io/github/ugaikit/vh/JsVirtualHomeClient.kt:11`, `:13`
- **Issue:** `timeout` is accepted as any `Int` and forwarded to delegate without range checks.
- **Impact:** Negative or unrealistic timeout values can cause unstable behavior (immediate failures, hangs, or misconfigured request timing).
- **Recommendation:** Validate timeout bounds in constructor (e.g., `timeout > 0` and sensible upper limit).

### 3) Low (Contextual Security): Endpoint parameters are accepted without trust constraints
- **Location:** `src/jsMain/kotlin/io/github/ugaikit/vh/JsVirtualHomeClient.kt:9-13`
- **Issue:** Wrapper accepts arbitrary `host`/`port` and forwards them directly to network client construction.
- **Impact:** If applications feed untrusted user input into this constructor, it can become an SSRF-style outbound request primitive.
- **Recommendation:** Treat host/port as trusted config only, or enforce allowlists/validation at integration boundaries.

## Optimization Findings (`./src`)

### 1) High: `Script.findObj` is O(n) per lookup, making parse path O(n^2)
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/Script.kt:55`, `:68-73`, `:87-94`
- **Finding:** Object dedup uses `MutableSet` + `firstOrNull` scan for each parsed object.
- **Performance Impact:** Large scripts with many object references degrade quadratically.
- **Optimization:** Replace lookup with `MutableMap<Pair<String, Int>, Obj>` (or equivalent key class) for O(1) average lookup.

### 2) Medium: Regex objects are recompiled on every `parseActionScript` call
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/ActionScript.kt:22`, `:27-28`
- **Finding:** Two `Regex(...)` instances are created each invocation.
- **Performance Impact:** Repeated parsing workloads incur avoidable regex compilation overhead.
- **Optimization:** Hoist regexes to top-level `private val` constants.

### 3) Medium: Eager Base64 decoding of all images can spike memory usage
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:138`
- **Finding:** `cameraImage()` decodes entire `messageList` eagerly into `List<ByteArray>`.
- **Performance Impact:** Large camera batches/images can create high peak heap usage.
- **Optimization:** Consider streaming/sequence-based decode or bounded batch decode APIs.

### 4) Medium: `runBlocking` wrapper incurs per-call bridge overhead in Java API
- **Location:** `src/jvmMain/kotlin/io/github/ugaikit/vh/JavaVirtualHomeClient.kt:21`, `:29`, `:36`, `:49`, `:64`, `:77`, `:92`, `:99`, `:107`, `:115`, `:132`
- **Finding:** Every Java-facing call creates a separate blocking coroutine bridge.
- **Performance Impact:** High-frequency Java integrations pay repeated dispatch/setup costs.
- **Optimization:** Add bulk/batched APIs or a dedicated execution context strategy to amortize crossing overhead.

### 5) Low/Medium: Filtering allocates intermediate collections where short-circuit lookup is enough
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/MainCommon.kt:172-175`, `:215-218`
- **Finding:** Uses `filter { ... }` to build a full list before selecting a single sofa entry.
- **Performance Impact:** Unnecessary list allocation and traversal in scene-query setup paths.
- **Optimization:** Use `firstOrNull`/`drop(index).firstOrNull()` patterns based on semantics.

### 6) Low: Verbose per-object logging in script validation can dominate runtime
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/Script.kt:116`, `:124`
- **Finding:** Logs action/object properties for each checked line/object.
- **Performance Impact:** In validation-heavy paths, log formatting and I/O can become the main cost.
- **Optimization:** Lower log level, guard with `isDebugEnabled`, or remove per-object logs in production paths.

### 7) Low: Always encoding default JSON fields increases request payload size
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:45`, `:168-171`, `:184-186`
- **Finding:** JSON is configured with `encodeDefaults = true`, so config defaults are always sent.
- **Performance Impact:** Slight but systematic network/payload overhead for frequent API calls.
- **Optimization:** If server compatibility allows, use `encodeDefaults = false` for smaller request bodies.
