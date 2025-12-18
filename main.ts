import * as fs from 'fs';
import * as vh from './build/compileSync/js/main/productionLibrary/kotlin/VirtualHome';

// Aliases for convenience
const com = vh.com;
const fujitsu = com.fujitsu;
const labs = fujitsu.labs;
const virtualhome = labs.virtualhome;
const kotlin = vh.kotlin;

const VirtualHomeClient = virtualhome.VirtualHomeClient;
const VirtualHomeRequest = virtualhome.VirtualHomeRequest;
const Position = virtualhome.Position;
const RenderParams = virtualhome.RenderParams;
const Graph = virtualhome.Graph;
const Node = virtualhome.Node;
const Edge = virtualhome.Edge;

type KtMutableList<E> = vh.kotlin.collections.KtMutableList<E>;
type KtList<E> = vh.kotlin.collections.KtList<E>;

// Helper to cast to MutableList which likely has 'add' at runtime
interface WritableKtMutableList<E> extends KtMutableList<E> {
    add(element: E): boolean;
}

// Helpers for Collections
function toJsArray<T>(collection: KtList<T> | KtMutableList<T> | any): T[] {
    if (Array.isArray(collection)) return collection;
    // Check for asJsArrayView or asJsReadonlyArrayView
    if (collection && typeof collection.asJsArrayView === 'function') {
        return collection.asJsArrayView();
    }
    if (collection && typeof collection.asJsReadonlyArrayView === 'function') {
        // Create a copy to return a standard Array
        return Array.from(collection.asJsReadonlyArrayView());
    }
    // Fallback
    return Array.from(collection || []);
}

function addToList<T>(collection: KtMutableList<T> | any, item: T) {
    if (collection && typeof collection.add === 'function') {
        collection.add(item);
    } else if (Array.isArray(collection) || (collection && typeof collection.push === 'function')) {
        collection.push(item);
    } else {
        throw new Error("Collection does not support add or push");
    }
}

function fromJsArray<T>(array: T[]): KtList<T> {
    return kotlin.collections.KtList.fromJsArray(array);
}

// Constants
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

class Main {
    sceneNum: number;
    client: vh.com.fujitsu.labs.virtualhome.VirtualHomeClient;

    constructor() {
        this.sceneNum = MAIN_SCENE_NUM;
        this.client = new VirtualHomeClient("localhost", 8080);
    }

    async testScripts(script: string[]): Promise<boolean> {
        let resetRes = await this.client.reset(this.sceneNum);
        if (!resetRes.success) throw new Error("Reset Error");

        let initGraph = await this.client.environmentGraph();
        let nodeList = toJsArray(initGraph.nodes);

        let sofas = nodeList.filter(it => it.className === "sofa");
        let sofa = sofas[sofas.length - 1]; // last()

        const newNode = new Node(
             CAT_ID,
             "Animals",
             "cat",
             null,
             null,
             null,
             fromJsArray([]),
             fromJsArray([])
        );

        addToList(initGraph.nodes, newNode);
        addToList(initGraph.edges, new Edge(CAT_ID, sofa.id!, "ON")); // sofa.id is Nullable<number>

        let expandRes = await this.client.expandScene(initGraph);
        if (expandRes.success) {
            console.log("Success : Expand Scene");
            let graph = await this.client.environmentGraph();
            let cats = toJsArray(graph.nodes).filter(it => it.className === "cat");
            let catId = cats[CAT_NODE_ID];
            console.log("CATID: " + catId);
        } else {
            console.log("Failed : Expand Scene");
        }

        let addCharRes = await this.client.addCharacter();
        if (!addCharRes.success) throw new Error("Add Character Error");
        console.log("Success : Add Character");

        let config = new RenderParams(
            false, // randomizeExecution
            -1, // randomSeed
            TEST_PROCESSING_TIME_LIMIT, // processingTimeLimit
            false, // skipExecution
            "Output/", // outputFolder
            "script", // fileNamePrefix
            5, // frameRate
            fromJsArray(["normal"]), // imageSynthesis
            false, // findSolution
            false, // savePoseData
            false, // saveSceneStatus
            fromJsArray(["AUTO"]), // cameraMode
            true, // recording
            640, // imageWidth
            480, // imageHeight
            1.0, // timeScale
            false // skipAnimation
        );

        // renderScript expects KtList<string>
        let res = await this.client.renderScript(fromJsArray(script), config);
        console.log(res.toString());
        return true;
    }

    async findNodes(name: string) {
        await this.client.reset(this.sceneNum);
        await this.client.addCharacter();
        let graph = await this.client.environmentGraph();
        let regex = new RegExp(name);
        let nodeList = toJsArray(graph.nodes);
        return nodeList.filter(it => it.className && regex.test(it.className));
    }

    async findNodesByProperty(property: string) {
        await this.client.reset(this.sceneNum);
        await this.client.addCharacter();
        let graph = await this.client.environmentGraph();
        let nodeList = toJsArray(graph.nodes);
        return nodeList.filter(it => it.properties && toJsArray(it.properties).includes(property));
    }

    async findNodesById(id: number) {
        await this.client.reset(this.sceneNum);
        await this.client.addCharacter();
        let graph = await this.client.environmentGraph();
        let nodeList = toJsArray(graph.nodes);
        return nodeList.filter(it => it.id === id);
    }
}

async function executeResetAndEnvironmentGraphRequests(sq: vh.com.fujitsu.labs.virtualhome.VirtualHomeClient) {
    let checkRes = await sq.check();
    console.log("Check: " + checkRes.success);
    let resetRes = await sq.reset(RESET_NUM);
    console.log(resetRes.success);
    for (let id of CAMERA_IDS) {
        let vo = await sq.visibleObjects(id);
        // vo is KtMap<string, string>.
        let map = vo.asJsReadonlyMapView();
        console.log(map.size);
    }
}

async function performCameraActions(sq: vh.com.fujitsu.labs.virtualhome.VirtualHomeClient) {
    let res = await sq.addCamera(new Position(POS_X, POS_Y, POS_Z), new Position(ROT_X, ROT_Y, ROT_Z));
    console.log(res.toString());
    console.log(await sq.cameraCount());
    let camData = await sq.cameraData(fromJsArray([CAMERA_ID]));
    console.log(camData.toString());
}

async function addCatToScene(sq: vh.com.fujitsu.labs.virtualhome.VirtualHomeClient, graph: vh.com.fujitsu.labs.virtualhome.Graph, sofa: vh.com.fujitsu.labs.virtualhome.Node) {
    let node = new Node(CAT_ID, "Animals", "cat", null, null, null, fromJsArray([]), fromJsArray([]));
    addToList(graph.nodes, node);

    let nodeList = toJsArray(graph.nodes);
    console.log("ADDRESSBOOK: " + nodeList.filter(it => it.className === "book").map(n=>n.toString()));

    addToList(graph.edges, new Edge(CAT_ID, sofa.id!, "ON"));
    console.log((await sq.expandScene(graph)).toString());

    let graph2 = await sq.environmentGraph();
    let nodeList2 = toJsArray(graph2.nodes);

    console.log(nodeList2.filter(it => it.id === CAT_ID).map(n=>n.toString()));
    console.log("CAT: " + nodeList2.filter(it => it.className === "cat").map(n=>n.toString()));

    let edgeList2 = toJsArray(graph2.edges);
    console.log(edgeList2.filter(it => it.toId === sofa.id).map(e=>e.toString()));

    console.log(nodeList2.length);
    console.log((await sq.addCharacter()).toString());
    console.log(await sq.cameraCount());
}

async function renderFinalScript(sq: vh.com.fujitsu.labs.virtualhome.VirtualHomeClient) {
    let config = new RenderParams(
        false, -1, PROCESSING_TIME_LIMIT, false, "Output/", "script", 5, fromJsArray(["normal"]),
        false, true, false, fromJsArray(["AUTO"]), true, 640, 480, 1.0, false
    );

    let script2 = [
        "<char0> [RUN] <book> (" + BOOK_ID + ")",
        "<char0> [FIND] <book> (" + BOOK_ID + ")",
        "<char0> [READ] <book> (" + BOOK_ID + ")",
        "<char0> [WALK] <sofa> (" + SOFA_ID_2 + ")",
        "<char0> [SIT] <sofa> (" + SOFA_ID_2 + ")",
    ];

    console.log((await sq.renderScript(fromJsArray(script2), config)).toString());
    await saveCameraImage(sq);
}

async function saveCameraImage(sq: vh.com.fujitsu.labs.virtualhome.VirtualHomeClient) {
    let res0 = await sq.cameraImage(fromJsArray([SAVE_CAMERA_ID]));
    // res0 is KtList<Int8Array>
    let list = toJsArray(res0);
    let image = list[0];
    if (image) {
        let buffer = Buffer.from(image);
        fs.writeFileSync("bfo.png", buffer);
    }
}

async function printNodeInformationAsync(main: Main) {
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
        let nodeList = toJsArray(graph.nodes);
        console.log(nodeList[MAIN_CAMERA_ID].toString());
        console.log(nodeList.length);

        let sofas = nodeList.filter(it => it.className === "sofa");
        let sofa = sofas[SOFA_INDEX];
        console.log(sofa.toString());

        await performCameraActions(sq);
        await addCatToScene(sq, graph, sofa);
        await renderFinalScript(sq);

    } catch (e: any) {
        console.error("Error in main:", e.message);
    }
}

main();
