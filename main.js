const vh = require('./build/compileSync/js/main/productionLibrary/kotlin/VirtualHome.js');
const fs = require('fs');

const io = vh.io;
const github = io.github;
const ugaikit = github.ugaikit;
const vhPkg = ugaikit.vh;

const VirtualHomeClient = vhPkg.VirtualHomeClient;
const VirtualHomeRequest = vhPkg.VirtualHomeRequest;
const Position = vhPkg.Position;
const RenderParams = vhPkg.RenderParams;
const Graph = vhPkg.Graph;
const Node = vhPkg.Node;
const Edge = vhPkg.Edge;

const CAT_ID = 1000;
const CAMERA_ID = 89;
const PROCESSING_TIME_LIMIT = 60;
const POS_X = 10;
const POS_Y = 200;
const POS_Z = 5;
const ROT_X = 20;
const ROT_Y = 21;
const ROT_Z = 22;
const RESET_NUM = 4;
const TEST_PROCESSING_TIME_LIMIT = 1;
const NODE_ID = 1;
const BOOK_ID = 86;
const SOFA_ID_2 = 139;
const WINE_ID = 93;
const TABLELAMP_ID = 76;
const CAM_0 = 0;
const CAM_1 = 1;
const CAM_2 = 2;
const CAM_3 = 3;
const CAMERA_IDS = [CAM_0, CAM_1, CAM_2, CAM_3];
const SAVE_CAMERA_ID = 0;
const CAT_NODE_ID = 0;
const MAIN_CAMERA_ID = 0;
const SOFA_INDEX = 1;
const MAIN_SCENE_NUM = 0;

/**
 * Helper to convert Kotlin collection to JS Array.
 */
function toArray(collection) {
    if (Array.isArray(collection)) {
        return collection;
    }
    if (collection && typeof collection.toArray === 'function') {
        return collection.toArray();
    }
    // Fallback if it's iterable but not an array or Kotlin collection with toArray
    return Array.from(collection || []);
}

/**
 * Helper to add an item to a collection (Kotlin MutableList or JS Array).
 */
function addItem(collection, item) {
    if (collection && typeof collection.add === 'function') {
        return collection.add(item);
    }
    if (collection && typeof collection.asJsArrayView === 'function') {
        return collection.asJsArrayView().push(item);
    }
    if (Array.isArray(collection) || (collection && typeof collection.push === 'function')) {
        return collection.push(item);
    }
    throw new Error("Collection does not support add or push: " + collection);
}

class Main {
    constructor() {
        this.sceneNum = MAIN_SCENE_NUM;
        this.client = new VirtualHomeClient("localhost", 8080);
    }

    async testScripts(script) {
        let resetRes = await this.client.reset(this.sceneNum);
        if (!resetRes.success) throw new Error("Reset Error");

        let initGraph = await this.client.environmentGraph();
        let nodeList = toArray(initGraph.nodes);

        let sofas = nodeList.filter(it => it.className === "sofa");
        let sofa = sofas[sofas.length - 1]; // last()

        // Create new Node
        // Kotlin: Node(id=..., category=..., className=..., ...)
        // Note: Constructor arguments are positional in JS export of Kotlin class.
        // Node(id, category, className, prefabName, objTransform, boundingBox, properties, states)
        const newNode = new Node(
             CAT_ID,
             "Animals",
             "cat",
             null,
             null,
             null,
             [],
             []
        );

        addItem(initGraph.nodes, newNode);
        addItem(initGraph.edges, new Edge(CAT_ID, sofa.id, "ON"));

        let expandRes = await this.client.expandScene(initGraph);
        if (expandRes.success) {
            console.log("Success : Expand Scene");
            let graph = await this.client.environmentGraph();
            let cats = toArray(graph.nodes).filter(it => it.className === "cat");
            let catId = cats[CAT_NODE_ID];
            console.log("CATID: " + catId);
        } else {
            console.log("Failed : Expand Scene");
        }

        let addCharRes = await this.client.addCharacter();
        if (!addCharRes.success) throw new Error("Add Character Error");
        console.log("Success : Add Character");

        // RenderParams(randomizeExecution, randomSeed, processingTimeLimit, skipExecution, outputFolder, fileNamePrefix, frameRate, imageSynthesis, findSolution, savePoseData, saveSceneStatus, cameraMode, recording, imageWidth, imageHeight, timeScale, skipAnimation, ...)
        let config = new RenderParams(
            false, // randomizeExecution
            -1, // randomSeed
            TEST_PROCESSING_TIME_LIMIT, // processingTimeLimit
            false, // skipExecution
            "Output/", // outputFolder
            "script", // fileNamePrefix
            5, // frameRate
            ["normal"], // imageSynthesis
            false, // findSolution
            false, // savePoseData
            false, // saveSceneStatus
            ["AUTO"], // cameraMode
            true, // recording
            640, // imageWidth
            480, // imageHeight
            1.0, // timeScale
            false // skipAnimation
        );

        let res = await this.client.renderScript(script, config);
        console.log(res.toString());
        return true;
    }

    async findNodes(name) {
        await this.client.reset(this.sceneNum);
        await this.client.addCharacter();
        let graph = await this.client.environmentGraph();
        let regex = new RegExp(name);
        let nodeList = toArray(graph.nodes);
        return nodeList.filter(it => it.className && regex.test(it.className));
    }

    async findNodesByProperty(property) {
        await this.client.reset(this.sceneNum);
        await this.client.addCharacter();
        let graph = await this.client.environmentGraph();
        let nodeList = toArray(graph.nodes);
        return nodeList.filter(it => it.properties && toArray(it.properties).includes(property));
    }

    async findNodesById(id) {
        await this.client.reset(this.sceneNum);
        await this.client.addCharacter();
        let graph = await this.client.environmentGraph();
        let nodeList = toArray(graph.nodes);
        return nodeList.filter(it => it.id === id);
    }
}

async function executeResetAndEnvironmentGraphRequests(sq) {
    let checkRes = await sq.check();
    console.log("Check: " + checkRes.success);
    let resetRes = await sq.reset(RESET_NUM);
    console.log(resetRes.success);
    for (let id of CAMERA_IDS) {
        let vo = await sq.visibleObjects(id);
        let size = 0;
        if (vo.size !== undefined) {
             size = vo.size;
        } else {
             size = Object.keys(vo).length;
        }
        console.log(size);
    }
}

async function performCameraActions(sq) {
    let res = await sq.addCamera(new Position(POS_X, POS_Y, POS_Z), new Position(ROT_X, ROT_Y, ROT_Z));
    console.log(res.toString());
    console.log(await sq.cameraCount());
    let camData = await sq.cameraData([CAMERA_ID]);
    console.log(camData.toString());
}

async function addCatToScene(sq, graph, sofa) {
    let node = new Node(CAT_ID, "Animals", "cat", null, null, null, [], []);
    addItem(graph.nodes, node);

    let nodeList = toArray(graph.nodes);
    console.log("ADDRESSBOOK: " + nodeList.filter(it => it.className === "book").map(n=>n.toString()));

    addItem(graph.edges, new Edge(CAT_ID, sofa.id, "ON"));
    console.log((await sq.expandScene(graph)).toString());

    let graph2 = await sq.environmentGraph();
    let nodeList2 = toArray(graph2.nodes);

    console.log(nodeList2.filter(it => it.id === CAT_ID).map(n=>n.toString()));
    console.log("CAT: " + nodeList2.filter(it => it.className === "cat").map(n=>n.toString()));

    let edgeList2 = toArray(graph2.edges);
    console.log(edgeList2.filter(it => it.toId === sofa.id).map(e=>e.toString()));

    console.log(nodeList2.length);
    console.log((await sq.addCharacter()).toString());
    console.log(await sq.cameraCount());
}

async function renderFinalScript(sq) {
    // Matches RenderParams constructor
    let config = new RenderParams(
        false, -1, PROCESSING_TIME_LIMIT, false, "Output/", "script", 5, ["normal"],
        false, true, false, ["AUTO"], true, 640, 480, 1.0, false
    );

    let script2 = [
        "<char0> [RUN] <book> (" + BOOK_ID + ")",
        "<char0> [FIND] <book> (" + BOOK_ID + ")",
        "<char0> [READ] <book> (" + BOOK_ID + ")",
        "<char0> [WALK] <sofa> (" + SOFA_ID_2 + ")",
        "<char0> [SIT] <sofa> (" + SOFA_ID_2 + ")",
    ];

    console.log((await sq.renderScript(script2, config)).toString());
    await saveCameraImage(sq);
}

async function saveCameraImage(sq) {
    let res0 = await sq.cameraImage([SAVE_CAMERA_ID]);
    let image = res0[0];
    if (image) {
        // Kotlin ByteArray -> JS Int8Array. fs.writeFileSync expects Buffer or Uint8Array.
        let buffer = Buffer.from(image);
        fs.writeFileSync("bfo.png", buffer);
    }
}

async function printNodeInformationAsync(main) {
    let nodes = await main.findNodes("tv");
    console.log(nodes.map(n=>n.toString()));

    nodes = await main.findNodesByProperty("HAS_PLUG");
    console.log(nodes.map(n=>n.toString()));

    nodes = await main.findNodesById(NODE_ID);
    console.log(nodes.map(n=>n.toString()));
}

async function main() {
    try {
        const script0 = [
            "<char0> [WALK] <wine> (" + WINE_ID + ")",
            "<char0> [GRAB] <wine> (" + WINE_ID + ")",
            " <char0> [DRINK] <wine> (" + WINE_ID + ")",
            "[DRINK] <wine> (" + WINE_ID + ")",
            "  [DRINK] <wine> (" + WINE_ID + ")",
            "<char0> [WALK] <tablelamp> (" + TABLELAMP_ID + ")",
        ];

        const mainInstance = new Main();
        await mainInstance.testScripts(script0);
        await printNodeInformationAsync(mainInstance);

        const sq = new VirtualHomeClient("localhost", 8080);

        let data = new VirtualHomeRequest(
            Math.floor(Math.random() * 100000),
            "idle"
        );

        let res = await sq.sendRequest(data);
        console.log(res.success);

        await executeResetAndEnvironmentGraphRequests(sq);

        let graph = await sq.environmentGraph();
        let nodeList = toArray(graph.nodes);
        console.log(nodeList[MAIN_CAMERA_ID].toString());
        console.log(nodeList.length);

        let sofas = nodeList.filter(it => it.className === "sofa");
        let sofa = sofas[SOFA_INDEX];
        console.log(sofa.toString());

        await performCameraActions(sq);
        await addCatToScene(sq, graph, sofa);
        await renderFinalScript(sq);

    } catch (e) {
        // If connection fails, we expect it in the sandbox environment.
        console.error("Error in main:", e.message);
    }
}

main();
