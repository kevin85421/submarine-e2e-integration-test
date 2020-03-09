/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.submarine.integration;

import org.apache.submarine.AbstractSubmarineIT;
import org.apache.submarine.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

public class registerIT extends AbstractSubmarineIT {

  public final static Logger LOG = LoggerFactory.getLogger(registerIT.class);

  @BeforeClass
  public static void startUp(){
    LOG.info("[Testcase]: registerIT");
    driver =  WebDriverManager.getWebDriver();
  }

  @AfterClass
  public static void tearDown(){
    driver.quit();
  }

  @Test
  public void registerFrontEndInvalidTest() throws Exception {
    // Navigate from Login page to Registration page
    LOG.info("Navigate from Login page to Registration page");
    pollingWait(By.xpath("//a[contains(text(), \"Create an account!\")]"), MAX_BROWSER_TIMEOUT_SEC).click();
    Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/user/register");

    // Username test
    //   Case1: empty username
    pollingWait(By.cssSelector("input[formcontrolname='username']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys(" \b");
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"Enter your username!\")]")).size(), 1);
    //   Case2: existed username
    pollingWait(By.cssSelector("input[formcontrolname='username']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys("test");
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"The username already exists!\")]")).size(), 1);

    // Email test
    //   Case1: empty email
    pollingWait(By.cssSelector("input[formcontrolname='email']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys(" \b");
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"Type your email!\")]")).size(), 1);
    //   Case2: existed email
    String existedEmailTestCase = "test@gmail.com";
    pollingWait(By.cssSelector("input[formcontrolname='email']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys(existedEmailTestCase);
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"The email is already used!\")]")).size(), 1); 
    //   Case3: invalid email
    String backspaceKeys = "";
    for ( int i=0; i < (existedEmailTestCase.length() - existedEmailTestCase.indexOf("@")); i++) {
        backspaceKeys += "\b";
    };
    pollingWait(By.cssSelector("input[formcontrolname='email']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys(backspaceKeys);
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"The email is invalid!\")]")).size(), 1); 
    
    // Password test
    //   Case1: empty password
    pollingWait(By.cssSelector("input[formcontrolname='password']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys(" \b");
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"Type your password!\")]")).size(), 1);
    //   Case2: string length must be in 6 ~ 20 characters
    pollingWait(By.cssSelector("input[formcontrolname='password']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys("testtesttesttesttesttest"); // length = 24
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"Password's length must be in 6 ~ 20 characters.\")]")).size(), 1);
    pollingWait(By.cssSelector("input[formcontrolname='password']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys("\b\b\b\b\b\b\b\b\b\b\b\b"); // length = 12
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"Password's length must be in 6 ~ 20 characters.\")]")).size(), 0);

    // Re-enter password test
    //   Case1: empty re-enter password
    pollingWait(By.cssSelector("input[formcontrolname='checkPassword']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys(" \b");
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"Type your password again!\")]")).size(), 1);
    //   Case2: re-enter password != password    
    pollingWait(By.cssSelector("input[formcontrolname='checkPassword']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys("1234"); // "1234" != "testtesttest"
    Assert.assertEquals( driver.findElements(By.xpath("//div[contains(text(), \"Passwords must match!\")]")).size(), 1);
    pollingWait(By.xpath("//a[@href='/user/login']"), MAX_BROWSER_TIMEOUT_SEC).click();
    Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/user/login"); 
  }

  @Test
  public void registerFrontEndValidTest() throws Exception {
    // Sign-Up successfully
    pollingWait(By.xpath("//a[contains(text(), \"Create an account!\")]"), MAX_BROWSER_TIMEOUT_SEC).click();
    Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/user/register");
    pollingWait(By.cssSelector("input[formcontrolname='username']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys("validusername");
    pollingWait(By.cssSelector("input[formcontrolname='email']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys("validemail@gmail.com");
    pollingWait(By.cssSelector("input[formcontrolname='password']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys("validpassword");
    pollingWait(By.cssSelector("input[formcontrolname='checkPassword']"), MAX_BROWSER_TIMEOUT_SEC).sendKeys("validpassword");
    pollingWait(By.cssSelector("label[formcontrolname='agree']"), MAX_BROWSER_TIMEOUT_SEC).click();
    pollingWait(By.cssSelector("button[class='ant-btn ant-btn-primary ant-btn-block']"), MAX_BROWSER_TIMEOUT_SEC).click(); 
    Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/user/login");
  }
}
