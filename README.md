# submarine-e2e-integration-test
Submarine E2E integration test is used to ensure the reliability of Submarine workbench (Angular version).

*   Why?
    *   We execute E2E testcases via **selenium**, but the behavior of **selenium** is very weird on travis. In addition, the result of the E2E tests with option **headless** 
*   Which test cases are different from apache/submarine?
    *   datadictIT.java