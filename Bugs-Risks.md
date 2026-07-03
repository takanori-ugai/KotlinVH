# Bugs and Risks Review for `./src`

This file now lists only findings that are still supported by the current sources.

## Additional Bug and Security Findings (`./src`)

### 1) High: Cancellation is swallowed in `sendRequest`
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:410-420`
- **Issue:** `catch (exception: Exception)` catches coroutine cancellation exceptions and converts them into normal failure responses.
- **Impact:** Breaks structured concurrency semantics; cancelled operations may continue as ordinary failures, causing stuck/shutdown issues.
- **Recommendation:** Re-throw `CancellationException` explicitly, and only wrap non-cancellation exceptions.

### 2) Medium: Unhandled Base64 decode failure can crash image retrieval
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:138`, `:424`
- **Issue:** `cameraImage()` decodes each payload item without guarding malformed Base64.
- **Impact:** A single malformed response element can throw and fail the whole call (reliability/DoS risk).
- **Recommendation:** Add per-item decode error handling and controlled failure behavior.

### 3) Medium: Several decode paths trust server JSON and can throw
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:260`, `:308`, `:365`, `:391`
- **Issue:** `environmentGraph`, `getVisibleObjects`, `visibleObjects`, and `characterCameras` decode JSON without parse guards.
- **Impact:** Malformed/unexpected server responses can crash callers.
- **Recommendation:** Apply consistent `SerializationException` handling as in `getObjects()`.

### 4) Medium: Script validator can throw `IndexOutOfBoundsException`
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/Script.kt:122-129`
- **Issue:** `action.properties[index]` is accessed for each parsed object without checking action arity.
- **Impact:** Script lines with extra object arguments crash validation instead of being rejected cleanly.
- **Recommendation:** Validate object count before indexing.

### 5) Medium: Script parser accepts partial matches
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/Script.kt:83`
- **Issue:** Parser uses `regex.find(line)` rather than full-line matching.
- **Impact:** Prefix-valid lines with trailing garbage can pass parsing, weakening input validation.
- **Recommendation:** Use `matchEntire` (or anchored regex) for strict parsing.

### 6) Medium: Crash-prone unchecked indexing in sample/runtime code
- **Location:** `src/jvmMain/kotlin/io/github/ugaikit/vh/Main.kt:138-140`, `:153`, `:169`, `:198`, `:213`; `src/jvmMain/java/io/github/ugaikit/vh/MainJava.java:44`
- **Issue:** Multiple direct index accesses (`list[0]`, `[idx]`, `.get(idx)`) without size checks.
- **Impact:** Unexpected scene/server states can trigger runtime crashes.
- **Recommendation:** Use `getOrNull`/`lastOrNull` or explicit bounds checks before access.

### 7) Low/Medium: Regex DoS potential in node search helper
- **Location:** `src/jvmMain/kotlin/io/github/ugaikit/vh/Main.kt:233-237`, `src/linuxX64Main/kotlin/io/github/ugaikit/vh/Main.kt:237-241`
- **Issue:** `Regex(name)` compiles caller-provided pattern directly.
- **Impact:** If exposed to untrusted input, complex patterns can cause excessive CPU usage.
- **Recommendation:** Escape input for literal matching or constrain regex complexity.

### 8) Low (Contextual Security): Plain HTTP transport by default
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:50`
- **Issue:** URL is hardcoded as `http://$host:$port`.
- **Impact:** Usually acceptable for local VirtualHome setups, but vulnerable to interception/tampering on untrusted networks.
- **Recommendation:** Keep compatibility, but document trust assumptions and optionally support configurable `http`/`https`.

### 9) Low: Exception details are printed and echoed in response
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:418-419`
- **Issue:** Raw exception text is logged with `println` and returned in response `message`.
- **Impact:** May expose internal runtime/network details to callers and logs.
- **Recommendation:** Use structured logging and sanitize external error messages.

## Incremental Findings From `src/commonMain`

### 1) Medium: Unbounded Base64 payload decoding can cause memory exhaustion
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:138`, `:424`
- **Issue:** `cameraImage()` decodes all `messageList` entries with no size/count guard.
- **Impact:** A malicious or misbehaving server can return very large Base64 payloads, leading to high memory pressure or OOM.
- **Recommendation:** Enforce maximum image count/size limits before decode, and fail fast when limits are exceeded.

### 2) Medium: Unbounded JSON parsing from server responses can amplify memory/CPU usage
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:260`, `:308`, `:365`, `:391`
- **Issue:** Server-provided JSON blobs are decoded without input-size limits.
- **Impact:** Large payloads can trigger heavy allocations and CPU spikes (DoS-style reliability risk).
- **Recommendation:** Add response-size constraints (where supported), and reject/short-circuit oversized payloads before full deserialization.

### 3) Low/Medium: `parseActionScript` silently drops malformed lines
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/ActionScript.kt:30-40`
- **Issue:** Parser uses `mapNotNull`, so non-matching lines are ignored instead of reported as errors.
- **Impact:** Validation bypass risk in upstream workflows: partially invalid scripts can be accepted with dropped actions, producing unintended behavior.
- **Recommendation:** Provide a strict mode that fails on first malformed line (or returns parse errors alongside parsed lines).

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

### 5) Medium: Repeated reset/addCharacter/graph-fetch round trips in query helpers
- **Location:** `src/jvmMain/kotlin/io/github/ugaikit/vh/Main.kt:228-261`, `src/linuxX64Main/kotlin/io/github/ugaikit/vh/Main.kt:232-265`
- **Finding:** `findNodes`, `findNodesByProperty`, and `findNodesById` each independently call `reset`, `addCharacter`, and `environmentGraph`.
- **Performance Impact:** 3 network operations per query, repeated across related calls.
- **Optimization:** Share a prepared graph snapshot or provide a combined query API that executes setup once.

### 6) Low/Medium: Filtering allocates intermediate collections where short-circuit lookup is enough
- **Location:** `src/jvmMain/kotlin/io/github/ugaikit/vh/Main.kt:67`, `:198`; `src/linuxX64Main/kotlin/io/github/ugaikit/vh/Main.kt:68`, `:202`
- **Finding:** Uses `filter { ... }[index]` instead of selection methods (`firstOrNull`, indexed scan).
- **Performance Impact:** Unnecessary list allocations and full-list traversal in hot/iterative paths.
- **Optimization:** Use `firstOrNull`/`drop(index).firstOrNull()` patterns based on semantics.

### 7) Low: Verbose per-object logging in script validation can dominate runtime
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/Script.kt:116`, `:124`
- **Finding:** Logs action/object properties for each checked line/object.
- **Performance Impact:** In validation-heavy paths, log formatting and I/O can become the main cost.
- **Optimization:** Lower log level, guard with `isDebugEnabled`, or remove per-object logs in production paths.

### 8) Low: Always encoding default JSON fields increases request payload size
- **Location:** `src/commonMain/kotlin/io/github/ugaikit/vh/VirtualHomeClient.kt:45`, `:168-171`, `:184-186`
- **Finding:** JSON is configured with `encodeDefaults = true`, so config defaults are always sent.
- **Performance Impact:** Slight but systematic network/payload overhead for frequent API calls.
- **Optimization:** If server compatibility allows, use `encodeDefaults = false` for smaller request bodies.
