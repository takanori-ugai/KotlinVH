package com.fujitsu.labs.virtualhome;

import java.util.List;
import org.junit.jupiter.api.Test;

public class JavaAddCharacterConfigTest {

  @Test
  public void test1() {
    Position position = new Position(1, 2, 3);
    System.out.println(new AddCharacterConfig("Chars/Male1", "fix_position", position, null));
    System.out.println(new AddCharacterConfig("Chars/Male1", "fix_position", null, "kitchen"));
    //        VirtualHomeClient client = new VirtualHomeClient();
    //        VirtualHomeResponse res = client.cameraImage(Arrays.asList(1), "normal", 640, 320) ;
  }

  @Test
  public void test2() {
    List<String> commons = Commons.INSTANCE.objectStates().get("razor");
    System.out.println(commons);
    new VirtualHomeClient("localhost", 8080);
  }
}
