const vh = require('./build/compileSync/js/main/productionLibrary/kotlin/VirtualHome.js');

console.log('Loaded module keys:', Object.keys(vh));

try {
    const io = vh.io;
    if (!io) {
        throw new Error("vh.io is undefined. Exports found: " + JSON.stringify(Object.keys(vh)));
    }
    const github = io.github;
    const ugaikit = github.ugaikit;
    const vhPkg = ugaikit.vh;
    const VirtualHomeClient = vhPkg.JsVirtualHomeClient;

    console.log('VirtualHomeClient found:', VirtualHomeClient);

    const client = new VirtualHomeClient("localhost", 8080);
    console.log('Client instantiated:', client);

    // Check if suspend functions are exported as async functions
    // cameraImage, cameraData, etc.
    // In generated JS, suspend functions usually return a Promise when called from JS if exported with @JsExport (since 2.3.0)
    // or take a continuation.
    // The user said: "They can be consumed as regular JavaScript async functions"

    console.log('client.cameraCount type:', typeof client.cameraCount);

    // Test async call
    (async () => {
        try {
            console.log("Calling cameraCount...");
            const result = client.cameraCount();
            console.log("Result type:", result.constructor.name);
            if (result instanceof Promise) {
                 console.log("It returns a Promise!");
            }
            const count = await result;
            console.log("Camera count:", count);
        } catch (e) {
            console.log("Error calling cameraCount (expected):", e.message);
        }
    })();

} catch (e) {
    console.error("Error accessing VirtualHomeClient:", e);
    // console.log("Full module dump:", vh);
}
