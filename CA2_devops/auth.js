// ================================================================
// auth.js — Firebase Authentication (Login / Signup / Logout)
// ES Module
// ================================================================

import { auth } from "./firebase-config.js";
import {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  onAuthStateChanged,
  signOut
} from "https://www.gstatic.com/firebasejs/10.8.0/firebase-auth.js";

// ── Export auth helpers for other modules ────────────────────────

/** Sign in an existing user. Returns the user credential. */
export async function loginUser(email, password) {
  return await signInWithEmailAndPassword(auth, email, password);
}

/** Register a new user. Returns the user credential. */
export async function registerUser(email, password) {
  return await createUserWithEmailAndPassword(auth, email, password);
}

/** Sign out the current user. */
export async function logoutUser() {
  return await signOut(auth);
}

/**
 * Guard a page — redirect to login.html if not authenticated.
 * @param {string} redirectTo  page to redirect to if not logged in
 */
export function requireAuth(redirectTo = "login.html") {
  onAuthStateChanged(auth, (user) => {
    if (!user) {
      window.location.href = redirectTo;
    }
  });
}

/**
 * Redirect away from login page if already authenticated.
 * @param {string} redirectTo  page to redirect logged-in users to
 */
export function redirectIfLoggedIn(redirectTo = "home.html") {
  onAuthStateChanged(auth, (user) => {
    if (user) {
      window.location.href = redirectTo;
    }
  });
}

/** Get the currently logged-in user (or null). */
export function getCurrentUser() {
  return auth.currentUser;
}
