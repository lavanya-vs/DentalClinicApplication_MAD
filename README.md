# DentIQ - Smart Dental Patient Registration System
 

DentalEase is a native Android application designed to streamline appointment handling, patient–doctor interaction, 
and clinic management tasks within a dental environment. It provides separate, role-based dashboards for Patients and Doctors, 
focusing primarily on efficient appointment booking, billing, medical history, and payment status tracking.

---

## 🧩 Key Features

- **Role-Based Access:**  
  Separate login flows and user interfaces for Patients and Doctors.

- **Patient Appointment Booking:**  
  Simple, easy-to-use forms for patients to view doctors, check availability, book appointments, 
  and track acceptance or reschedule status.

- **Doctor Management Dashboard:**  
  A dynamic dashboard for doctors to view booked appointments, accept or reschedule, 
  check patient history, update billing, and mark payment completion.

- **Billing & Payment Tracking:**  
  Doctors can generate bills, patients can view them, and payments can be simulated using a QR module.  
  Doctors get real-time updates when a patient completes the payment.

- **Medical History:**  
  Doctors can view patient visit history, symptoms, diagnosis notes, and updated records.

- **Dental Care Guide:**  
  Patients can access a general oral-care guide for tips, hygiene suggestions, and awareness.

- **Modern UI:**  
  Clean, aesthetic, gradient-based interface styled using native Android views and Kotlin.  
  Inspired by minimal Pinterest-style design.

---

## 🛠 Technology Stack

- **Language:** Kotlin  
- **Platform:** Native Android Development  
- **IDE:** Android Studio  
- **Architecture:** MVVM  
- **Local Data:** Room Database  
- **Data Serialization:** Gson  
- **UI Elements:** XML + Material 3 + Gradient components  
- **Payment Simulation:** Dummy QR-based UI  
- **Visualization (optional future):** Charts for appointment stats or billing trends  

---

## 📁 Modules Overview

| Module | Description |
|--------|-------------|
| **PatientAppointmentActivity** | Handles patient appointment creation and viewing. |
| **DoctorDashboardActivity** | Dashboard showing appointments, patient stats, and quick action buttons. |
| **AppointmentDetailActivity** | For doctors to approve/reschedule patient requests. |
| **PatientHistoryActivity** | Doctors view full medical history including past visits. |
| **BillingActivity** | The interface for the doctor to add/update billing, mark payments. |
| **PaymentActivity** | Patients can pay through dummy QR screen and update payment status. |
| **CareGuideActivity** | Contains all oral-care information (tips, hygiene routines, awareness). |

---

## 🤝 Contribution

If you’d like to suggest enhancements or improvements, feel free to open an issue or submit a pull request!

### 👣 Steps to Contribute:

1. **Fork this Repository**  
2. **Create your Feature Branch**  
   (`git checkout -b feature/newFeature`)  
3. **Commit your Changes**  
4. **Push to your Branch**  
5. **Open a Pull Request** 🚀

---
