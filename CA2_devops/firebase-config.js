// ================================================================
// firebase-config.js — Firebase Initialization
// Firebase Modular SDK v10 via CDN
// ================================================================

import { initializeApp }  from "https://www.gstatic.com/firebasejs/10.8.0/firebase-app.js";
import { getAnalytics }   from "https://www.gstatic.com/firebasejs/10.8.0/firebase-analytics.js";
import { getFirestore }   from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";
import { getAuth }        from "https://www.gstatic.com/firebasejs/10.8.0/firebase-auth.js";

const firebaseConfig = {
  apiKey:            "AIzaSyCEaQ8qa2UhSvL5o3cp5rmhC-11gV9IkXI",
  authDomain:        "feedback-634cb.firebaseapp.com",
  projectId:         "feedback-634cb",
  storageBucket:     "feedback-634cb.firebasestorage.app",
  messagingSenderId: "350406774383",
  appId:             "1:350406774383:web:186096244aa257fff8479a",
  measurementId:     "G-XCC27Q63GJ"
};

const app            = initializeApp(firebaseConfig);
export const analytics = getAnalytics(app);
export const db        = getFirestore(app);
export const auth      = getAuth(app);
