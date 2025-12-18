package io.github.ugaikit.vh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainJava {
  private static final int RESET_NUM = 4;
  private static final int POS_X = 10;
  private static final int POS_Y = 200;
  private static final int POS_Z = 5;
  private static final int ROT_X = 20;
  private static final int ROT_Y = 21;
  private static final int ROT_Z = 22;
  private static final int CAMERA_ID = 89;
  private static final int MAIN_CAMERA_ID = 0;
  private static final int SOFA_INDEX = 1;
  private static final int CAT_ID = 1000;
  private static final int BOOK_ID = 86;
  private static final int SOFA_ID_2 = 139;
  private static final int PROCESSING_TIME_LIMIT = 60;

  public static void main(String[] args) {
    JavaVirtualHomeClient client = new JavaVirtualHomeClient("localhost", 8080);

    System.out.println("Check: " + client.check().getSuccess());
    System.out.println("Reset: " + client.reset(RESET_NUM).getSuccess());

    Graph graph = client.environmentGraph();
    System.out.println("Node at MAIN_CAMERA_ID: " + graph.getNodes().get(MAIN_CAMERA_ID));
    System.out.println("Total nodes: " + graph.getNodes().size());

    List<Node> sofas =
        graph.getNodes().stream()
            .filter(node -> "sofa".equals(node.getClassName()))
            .collect(Collectors.toList());

    if (sofas.size() > SOFA_INDEX) {
      Node sofa = sofas.get(SOFA_INDEX);
      System.out.println("Sofa: " + sofa);

      performCameraActions(client);
      addCatToScene(client, graph, sofa);
      renderFinalScript(client);
    } else {
      System.out.println("Sofa not found at index " + SOFA_INDEX);
    }
  }

  private static void performCameraActions(JavaVirtualHomeClient client) {
    Position pos = new Position(POS_X, POS_Y, POS_Z);
    Position rot = new Position(ROT_X, ROT_Y, ROT_Z);
    System.out.println("Add Camera: " + client.addCamera(pos, rot, 40));
    System.out.println("Camera Count: " + client.cameraCount());
    System.out.println("Camera Data: " + client.cameraData(Arrays.asList(CAMERA_ID)));
  }

  private static void addCatToScene(JavaVirtualHomeClient client, Graph graph, Node sofa) {
    // Node constructor: id, category, className, prefabName, objTransform, boundingBox, properties,
    // states
    // In Kotlin: val id: Int? = null, val category: String? = null, ...
    // In Java, without @JvmOverloads, we must pass all arguments.

    Node node =
        new Node(
            CAT_ID, // id
            "Animals", // category
            "cat", // className
            null, // prefabName
            null, // objTransform
            null, // boundingBox
            new ArrayList<>(), // properties
            new ArrayList<>() // states
            );
    graph.getNodes().add(node);

    graph.getEdges().add(new Edge(CAT_ID, sofa.getId(), "ON"));

    System.out.println("Expand Scene: " + client.expandScene(graph, new ExpandSceneConfig()));

    Graph graph2 = client.environmentGraph();
    System.out.println("Graph2 nodes size: " + graph2.getNodes().size());

    System.out.println("Add Character: " + client.addCharacter("Chars/Male1", null, ""));
    System.out.println("Camera Count after char: " + client.cameraCount());
  }

  private static void renderFinalScript(JavaVirtualHomeClient client) {
    RenderParams config =
        client.createRenderParams(
            PROCESSING_TIME_LIMIT,
            false, // findSolution
            false, // skipAnimation
            true, // recording
            true, // savePoseData
            false // skipExecution
            );

    List<String> script =
        Arrays.asList(
            "<char0> [RUN] <book> (" + BOOK_ID + ")",
            "<char0> [FIND] <book> (" + BOOK_ID + ")",
            "<char0> [READ] <book> (" + BOOK_ID + ")",
            "<char0> [WALK] <sofa> (" + SOFA_ID_2 + ")",
            "<char0> [SIT] <sofa> (" + SOFA_ID_2 + ")");

    System.out.println("Render Script: " + client.renderScript(script, config));
  }
}
