/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import junit.framework.TestCase;

/**
 * Basic test to verify serialization support.
 *
 * @author Tom Ball
 */
public class SerializationTest extends TestCase {

  private static final String TEST_FILE_NAME = "serialization-test.bin";

  static class Greeting implements Serializable {
    private String greeting;
    private String name;
    private transient int n;

    public Greeting(String greeting, String name, int n) {
      this.greeting = greeting;
      this.name = name;
      this.n = n;
    }

    @Override
    public String toString() {
      return String.format("%s, %s!", greeting, name);
    }
  }

  @Override
  protected void tearDown() throws Exception {
    //new File(TEST_FILE_NAME).delete();
    super.tearDown();
  }

  public void testSerialization() throws IOException, ClassNotFoundException {
    Greeting greeting = new Greeting("hello", "world", 42);
    assertEquals("hello, world!", greeting.toString());
    assertEquals(42, greeting.n);

    // Save the greeting to a file.
    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEST_FILE_NAME));
    out.writeObject(greeting);
    out.close();
    File binFile = new File(TEST_FILE_NAME);
    assertTrue(binFile.exists());

    // Read back the greeting.
    ObjectInputStream in = new ObjectInputStream(new FileInputStream(TEST_FILE_NAME));
    Greeting greeting2 = (Greeting) in.readObject();
    in.close();
    assertEquals("hello, world!", greeting.toString());
    assertEquals(0, greeting2.n);  // 0 because n is transient.
  }
}
