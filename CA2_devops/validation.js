// ================================================================
// validation.js — Form Validation + Firebase Firestore Integration
// ES Module (loaded via type="module" in index.html)
// ================================================================

import { db } from "./firebase-config.js";
import {
  collection,
  addDoc,
  serverTimestamp
} from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

// ── DOM References ───────────────────────────────────────────────
const form          = document.getElementById('feedbackForm');
const nameField     = document.getElementById('studentName');
const emailField    = document.getElementById('email');
const mobileField   = document.getElementById('mobile');
const deptField     = document.getElementById('department');
const feedbackField = document.getElementById('feedback');
const wordCountEl   = document.getElementById('wordCount');
const toast         = document.getElementById('toast');
const submitBtn     = document.getElementById('submitBtn');

// ── Helpers ──────────────────────────────────────────────────────

/** Count words in a string. */
function countWords(str) {
  return str.trim().split(/\s+/).filter(w => w.length > 0).length;
}

/**
 * Show/clear inline error for a field.
 * @param {HTMLElement} field
 * @param {HTMLElement} errorEl
 * @param {string}      message  — empty string = clear error
 */
function setError(field, errorEl, message) {
  errorEl.textContent = message;
  if (message) {
    field.classList.add('invalid');
  } else {
    field.classList.remove('invalid');
  }
}

/**
 * Display a toast notification.
 * @param {string} message
 * @param {'success'|'error'|'info'} type
 */
function showToast(message, type = 'success') {
  toast.textContent = message;
  toast.className = `toast ${type} show`;
  setTimeout(() => toast.classList.remove('show'), 4000);
}

// ── Live Word Counter ────────────────────────────────────────────
feedbackField.addEventListener('input', () => {
  const count = countWords(feedbackField.value);
  wordCountEl.textContent = `${count} word${count !== 1 ? 's' : ''}`;
  wordCountEl.style.color = count >= 10 ? '#38a169' : '#718096';
});

// ── Individual Validators ────────────────────────────────────────

function validateName() {
  const val = nameField.value.trim();
  const err = document.getElementById('nameError');
  if (!val) {
    setError(nameField, err, '⚠ Student name cannot be empty.');
    return false;
  }
  setError(nameField, err, '');
  return true;
}

function validateEmail() {
  const val = emailField.value.trim();
  const err = document.getElementById('emailError');
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!val) {
    setError(emailField, err, '⚠ Email ID cannot be empty.');
    return false;
  }
  if (!emailRegex.test(val)) {
    setError(emailField, err, '⚠ Please enter a valid email address (e.g. abc@domain.com).');
    return false;
  }
  setError(emailField, err, '');
  return true;
}

function validateMobile() {
  const val = mobileField.value.trim();
  const err = document.getElementById('mobileError');
  const mobileRegex = /^\d{10}$/;
  if (!val) {
    setError(mobileField, err, '⚠ Mobile number cannot be empty.');
    return false;
  }
  if (!mobileRegex.test(val)) {
    setError(mobileField, err, '⚠ Mobile number must contain exactly 10 digits only.');
    return false;
  }
  setError(mobileField, err, '');
  return true;
}

function validateDepartment() {
  const val = deptField.value;
  const err = document.getElementById('deptError');
  if (!val) {
    setError(deptField, err, '⚠ Please select a department.');
    return false;
  }
  setError(deptField, err, '');
  return true;
}

function validateGender() {
  const selected = document.querySelector('input[name="gender"]:checked');
  const err = document.getElementById('genderError');
  if (!selected) {
    err.textContent = '⚠ Please select a gender option.';
    return false;
  }
  err.textContent = '';
  return true;
}

function validateFeedback() {
  const val = feedbackField.value.trim();
  const err = document.getElementById('feedbackError');
  if (!val) {
    setError(feedbackField, err, '⚠ Feedback comments cannot be blank.');
    return false;
  }
  if (countWords(val) < 10) {
    setError(feedbackField, err, '⚠ Feedback must be at least 10 words long.');
    return false;
  }
  setError(feedbackField, err, '');
  return true;
}

// ── Blur Validation (UX: validate as user leaves each field) ─────
nameField.addEventListener('blur',     validateName);
emailField.addEventListener('blur',    validateEmail);
mobileField.addEventListener('blur',   validateMobile);
deptField.addEventListener('blur',     validateDepartment);
feedbackField.addEventListener('blur', validateFeedback);

// ── Save to Firestore ────────────────────────────────────────────
async function saveFeedbackToFirestore(data) {
  const docRef = await addDoc(collection(db, "feedback"), {
    "Student Name":  data.studentName,
    "Email":         data.email,
    "Mobile Number": data.mobile,
    "Department":    data.department,
    "Gender":        data.gender,
    "Feedback":      data.feedback,
    "Time":          serverTimestamp()
  });
  return docRef.id;
}

// ── Form Submit ──────────────────────────────────────────────────
form.addEventListener('submit', async (e) => {
  e.preventDefault();

  const isValid = [
    validateName(),
    validateEmail(),
    validateMobile(),
    validateDepartment(),
    validateGender(),
    validateFeedback()
  ].every(Boolean);

  if (!isValid) {
    showToast('❌ Please fix the errors before submitting.', 'error');
    const firstInvalid = form.querySelector('.invalid');
    if (firstInvalid) {
      firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
      firstInvalid.focus();
    }
    return;
  }

  // ── Collect form data ────────────────────────────────────────
  const formData = {
    studentName: nameField.value.trim(),
    email:       emailField.value.trim(),
    mobile:      mobileField.value.trim(),
    department:  deptField.options[deptField.selectedIndex].text,
    gender:      document.querySelector('input[name="gender"]:checked').value,
    feedback:    feedbackField.value.trim()
  };

  // ── Disable button & show loading state ──────────────────────
  submitBtn.disabled    = true;
  submitBtn.textContent = '⏳ Submitting...';

  try {
    const docId = await saveFeedbackToFirestore(formData);
    console.log("Feedback saved with ID:", docId);

    showToast('✅ Feedback submitted and saved successfully! Thank you.', 'success');

    // Reset form after short delay
    setTimeout(() => {
      form.reset();
      wordCountEl.textContent = '0 words';
      wordCountEl.style.color = '#718096';
    }, 600);

  } catch (error) {
    console.error("Firestore error:", error);
    showToast('⚠ Feedback valid, but failed to save. Check your connection.', 'error');
  } finally {
    // Re-enable submit button
    submitBtn.disabled    = false;
    submitBtn.textContent = '✔ Submit';
  }
});

// ── Reset Button — clear all error states ────────────────────────
document.getElementById('resetBtn').addEventListener('click', () => {
  form.querySelectorAll('.error-msg').forEach(el => el.textContent = '');
  form.querySelectorAll('.invalid').forEach(el  => el.classList.remove('invalid'));
  wordCountEl.textContent = '0 words';
  wordCountEl.style.color = '#718096';
});
