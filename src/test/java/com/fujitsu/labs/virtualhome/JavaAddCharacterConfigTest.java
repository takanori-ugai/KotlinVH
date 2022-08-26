package com.fujitsu.labs.virtualhome;

import mu.KLogger;
import mu.KotlinLogging;
import org.junit.jupiter.api.Test;

public class JavaAddCharacterConfigTest {

    /*
    @kotlinx.serialization.ExperimentalSerializationApi
    private Json format = Json.Default. {
        encodeDefaults = true
        explicitNulls = false
    }
    */

    @Test
    public void test1() {
        Position position = new Position(1, 2, 3);
        System.out.println(
                new AddCharacterConfig(
                        "Chars/Male1",
                        "fix_position",
                        position,
                        null
                )
        );
        System.out.println(
                new AddCharacterConfig(
                        "Chars/Male1",
                        "fix_position",
                        null,
                        "kitchen"
                )
        );
//        VirtualHomeClient client = new VirtualHomeClient();
//        VirtualHomeResponse res = client.cameraImage(Arrays.asList(1), "normal", 640, 320) ;
    }
}
