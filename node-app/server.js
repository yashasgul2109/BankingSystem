const express = require("express");
const nodemailer = require("nodemailer");
require("dotenv").config();

const app = express();

app.use(express.json());

const transporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASS
    }
});

app.post("/send-email", async (req, res) => {
    const { to, subject, message } = req.body;

    try {
        const mailOptions = {
            from: process.env.EMAIL_USER,
            to: to,
            subject: subject,
            text: message
        };

        await transporter.sendMail(mailOptions);

        res.status(200).json({
            message: "Email sent successfully"
        });
    } catch (error) {
        console.log(error);

        res.status(500).json({
            message: "Failed to send email"
        });
    }
});

app.listen(3000, () => {
    console.log("Node.js server running on port 3000");
});