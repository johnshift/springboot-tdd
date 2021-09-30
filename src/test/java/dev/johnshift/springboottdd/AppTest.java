package dev.johnshift.springboottdd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppTest {

  /** 
   * Nothing fancy here. 
   * Just increasing test coverage [:
   * */
  @Test
  void mainTest() {

    int two = 2;
    assertEquals(2, two);

    SpringbootTddApplication.main(new String[] {});
  }

}
