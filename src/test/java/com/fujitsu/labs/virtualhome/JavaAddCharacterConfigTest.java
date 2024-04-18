package com.fujitsu.labs.virtualhome;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;

/** This class is used to test the AddCharacterConfig functionality. */
public class JavaAddCharacterConfigTest {

  /**
   * This test checks the AddCharacterConfig functionality with different position and room
   * parameters.
   */
  @Test
  public void test1() {
    Position position = new Position(1, 2, 3);
    AddCharacterConfig config1 =
        new AddCharacterConfig("Chars/Male1", AddCharacterMode.FixPosition.toString(), position, null);
    AddCharacterConfig config2 =
        new AddCharacterConfig("Chars/Male1", AddCharacterMode.FixPosition.toString(), null, "kitchen");

    assertNotNull(config1);
    assertNotNull(config2);

    System.out.println(config1);
    System.out.println(config2);

    //        VirtualHomeClient client = new VirtualHomeClient();
    //        VirtualHomeResponse res = client.cameraImage(Arrays.asList(1), "normal", 640, 320) ;
  }

  /**
   * This test checks the objectStates functionality of the Commons class and the VirtualHomeClient
   * constructor.
   */
  @Test
  public void test2() {
    List<String> commons = Commons.INSTANCE.objectStates().get("razor");

    assertNotNull(commons);

    VirtualHomeClient client = new VirtualHomeClient("localhost", 8080);

    assertNotNull(client);
  }
}
