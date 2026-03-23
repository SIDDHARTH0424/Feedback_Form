# =================================================================
# test_feedback_form.py
# Selenium Test Suite — Student Feedback Registration Form
# =================================================================
# Requirements:
#   pip install selenium webdriver-manager pytest pytest-html
#
# Run:
#   python -m pytest test_feedback_form.py -v
# =================================================================

import os
import unittest
import time

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.support.ui import Select, WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager


# ── Absolute path to the form HTML file ──────────────────────────
HTML_FILE = os.path.abspath(
    os.path.join(os.path.dirname(__file__), "index.html")
)
PAGE_URL = f"file:///{HTML_FILE.replace(os.sep, '/')}"


def build_driver():
    """Create a headless Chrome WebDriver instance."""
    options = webdriver.ChromeOptions()
    options.add_argument("--headless=new")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    options.add_argument("--window-size=1280,900")
    driver = webdriver.Chrome(
        service=ChromeService(ChromeDriverManager().install()),
        options=options
    )
    return driver


class TestFeedbackForm(unittest.TestCase):
    """Automated Selenium tests for the Student Feedback Registration Form."""

    @classmethod
    def setUpClass(cls):
        """Launch browser once for all tests."""
        cls.driver = build_driver()
        cls.wait = WebDriverWait(cls.driver, 8)

    @classmethod
    def tearDownClass(cls):
        """Quit browser after all tests."""
        cls.driver.quit()

    def setUp(self):
        """Navigate to the form and reset it before each test."""
        self.driver.get(PAGE_URL)
        self.wait.until(EC.presence_of_element_located((By.ID, "feedbackForm")))

    # ── Helper: fill a valid complete form ────────────────────────
    def _fill_valid_form(self):
        self.driver.find_element(By.ID, "studentName").send_keys("Rahul Sharma")
        self.driver.find_element(By.ID, "email").send_keys("rahul.sharma@example.com")
        self.driver.find_element(By.ID, "mobile").send_keys("9876543210")
        Select(self.driver.find_element(By.ID, "department")).select_by_value("cs")
        self.driver.find_element(By.ID, "genderMale").click()
        self.driver.find_element(By.ID, "feedback").send_keys(
            "This course was excellent and very informative. I learned a lot from the faculty and practical sessions."
        )

    # ── Helper: click submit and wait briefly ─────────────────────
    def _submit(self):
        self.driver.find_element(By.ID, "submitBtn").click()
        time.sleep(0.4)

    # ─────────────────────────────────────────────────────────────
    # TEST 1: Empty Form Submission
    # ─────────────────────────────────────────────────────────────
    def test_01_empty_form_submission(self):
        """Submitting an empty form should show error for Student Name."""
        self._submit()
        err = self.driver.find_element(By.ID, "nameError").text
        self.assertIn("empty", err.lower(),
                      f"Expected name error not shown. Got: '{err}'")

    # ─────────────────────────────────────────────────────────────
    # TEST 2: Invalid Email Format
    # ─────────────────────────────────────────────────────────────
    def test_02_invalid_email(self):
        """An improperly formatted email should trigger the email error."""
        self.driver.find_element(By.ID, "studentName").send_keys("Jane Doe")
        self.driver.find_element(By.ID, "email").send_keys("notanemail")
        self._submit()
        err = self.driver.find_element(By.ID, "emailError").text
        self.assertTrue(
            len(err) > 0,
            "Expected email validation error but found none."
        )
        self.assertIn("valid", err.lower(),
                      f"Unexpected email error text: '{err}'")

    # ─────────────────────────────────────────────────────────────
    # TEST 3: Invalid Mobile Number (non-digit characters)
    # ─────────────────────────────────────────────────────────────
    def test_03_invalid_mobile_number(self):
        """A mobile number with letters should trigger the mobile error."""
        self.driver.find_element(By.ID, "studentName").send_keys("John Smith")
        self.driver.find_element(By.ID, "email").send_keys("john@example.com")
        self.driver.find_element(By.ID, "mobile").send_keys("ABCDE12345")
        self._submit()
        err = self.driver.find_element(By.ID, "mobileError").text
        self.assertTrue(len(err) > 0,
                        "Expected mobile validation error but found none.")

    # ─────────────────────────────────────────────────────────────
    # TEST 4: Mobile Number Too Short
    # ─────────────────────────────────────────────────────────────
    def test_04_mobile_too_short(self):
        """A mobile number with fewer than 10 digits should be invalid."""
        self.driver.find_element(By.ID, "studentName").send_keys("Priya Patel")
        self.driver.find_element(By.ID, "email").send_keys("priya@example.com")
        self.driver.find_element(By.ID, "mobile").send_keys("12345")
        self._submit()
        err = self.driver.find_element(By.ID, "mobileError").text
        self.assertTrue(len(err) > 0,
                        "Expected mobile length error but found none.")

    # ─────────────────────────────────────────────────────────────
    # TEST 5: No Gender Selected
    # ─────────────────────────────────────────────────────────────
    def test_05_missing_gender(self):
        """Skipping gender selection should show the gender error."""
        self.driver.find_element(By.ID, "studentName").send_keys("Anita Rao")
        self.driver.find_element(By.ID, "email").send_keys("anita@example.com")
        self.driver.find_element(By.ID, "mobile").send_keys("9123456780")
        Select(self.driver.find_element(By.ID, "department")).select_by_value("it")
        # Intentionally skip gender
        self.driver.find_element(By.ID, "feedback").send_keys(
            "Very good course, enjoyed the sessions and all the assignments given."
        )
        self._submit()
        err = self.driver.find_element(By.ID, "genderError").text
        self.assertTrue(len(err) > 0,
                        "Expected gender error but found none.")

    # ─────────────────────────────────────────────────────────────
    # TEST 6: No Department Selected
    # ─────────────────────────────────────────────────────────────
    def test_06_missing_department(self):
        """Leaving department at default should show the department error."""
        self.driver.find_element(By.ID, "studentName").send_keys("Vikram Kumar")
        self.driver.find_element(By.ID, "email").send_keys("vikram@example.com")
        self.driver.find_element(By.ID, "mobile").send_keys("8899001122")
        # Intentionally skip department selection
        self.driver.find_element(By.ID, "genderMale").click()
        self.driver.find_element(By.ID, "feedback").send_keys(
            "The professors are excellent and explain concepts in a very clear way."
        )
        self._submit()
        err = self.driver.find_element(By.ID, "deptError").text
        self.assertTrue(len(err) > 0,
                        "Expected department error but found none.")

    # ─────────────────────────────────────────────────────────────
    # TEST 7: Feedback Too Short (fewer than 10 words)
    # ─────────────────────────────────────────────────────────────
    def test_07_short_feedback(self):
        """Feedback with fewer than 10 words should trigger the feedback error."""
        self.driver.find_element(By.ID, "studentName").send_keys("Sara Khan")
        self.driver.find_element(By.ID, "email").send_keys("sara@example.com")
        self.driver.find_element(By.ID, "mobile").send_keys("7011223344")
        Select(self.driver.find_element(By.ID, "department")).select_by_value("ec")
        self.driver.find_element(By.ID, "genderFemale").click()
        self.driver.find_element(By.ID, "feedback").send_keys("Good course.")   # < 10 words
        self._submit()
        err = self.driver.find_element(By.ID, "feedbackError").text
        self.assertTrue(len(err) > 0,
                        "Expected feedback length error but found none.")
        self.assertIn("10", err,
                      f"Error message should mention '10'. Got: '{err}'")

    # ─────────────────────────────────────────────────────────────
    # TEST 8: Valid Form Submission
    # ─────────────────────────────────────────────────────────────
    def test_08_valid_form_submission(self):
        """A fully valid form should show a success toast and no errors."""
        self._fill_valid_form()
        self._submit()

        # Check no error messages are visible
        error_ids = ["nameError", "emailError", "mobileError",
                     "deptError", "genderError", "feedbackError"]
        for eid in error_ids:
            err_text = self.driver.find_element(By.ID, eid).text
            self.assertEqual(err_text, "",
                             f"Unexpected error in '{eid}': {err_text}")

        # Check success toast appears
        toast = self.wait.until(EC.visibility_of_element_located((By.ID, "toast")))
        toast_text = toast.text
        self.assertIn("success", toast_text.lower(),
                      f"Toast should indicate success. Got: '{toast_text}'")

    # ─────────────────────────────────────────────────────────────
    # TEST 9: Reset Button Clears the Form
    # ─────────────────────────────────────────────────────────────
    def test_09_reset_button(self):
        """Reset button should clear all field values."""
        self._fill_valid_form()
        self.driver.find_element(By.ID, "resetBtn").click()
        time.sleep(0.3)

        name_val   = self.driver.find_element(By.ID, "studentName").get_attribute("value")
        email_val  = self.driver.find_element(By.ID, "email").get_attribute("value")
        mobile_val = self.driver.find_element(By.ID, "mobile").get_attribute("value")

        self.assertEqual(name_val,   "", "Name field should be cleared after reset.")
        self.assertEqual(email_val,  "", "Email field should be cleared after reset.")
        self.assertEqual(mobile_val, "", "Mobile field should be cleared after reset.")


if __name__ == "__main__":
    unittest.main(verbosity=2)
