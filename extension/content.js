console.log("ReplyCraft extension loaded");

async function getAccessToken() {
  return new Promise((resolve) => {
    chrome.storage.local.get(["accessToken"], (result) => {
      resolve(result.accessToken);
    });
  });
}

function injectButtons() {
  // ONLY reply boxes
  const replyContainers = document.querySelectorAll(".gA.gt");

  replyContainers.forEach((container) => {
    const toolbar = container.querySelector(".aDh");

    if (!toolbar) return;

    if (toolbar.querySelector(".replycraft-btn")) {
      return;
    }

    const sendButton = toolbar.querySelector(".T-I.J-J5-Ji.aoO.v7.T-I-atl.L3");

    if (!sendButton) return;

    const btn = document.createElement("div");

    btn.innerText = "Generate Reply";

    btn.className = "replycraft-btn";

    btn.style.marginLeft = "8px";
    btn.style.padding = "8px 14px";
    btn.style.border = "1px solid #dadce0";
    btn.style.borderRadius = "18px";
    btn.style.cursor = "pointer";
    btn.style.backgroundColor = "#ffffff";
    btn.style.color = "#444";
    btn.style.fontSize = "14px";
    btn.style.fontWeight = "500";
    btn.style.display = "flex";
    btn.style.alignItems = "center";
    btn.style.justifyContent = "center";
    btn.style.userSelect = "none";
    btn.style.transition = "0.2s";

    btn.addEventListener("mouseenter", () => {
      btn.style.backgroundColor = "#f1f3f4";
    });

    btn.addEventListener("mouseleave", () => {
      btn.style.backgroundColor = "#ffffff";
    });

    btn.addEventListener("click", async () => {
      try {
        const token = await getAccessToken();

        if (!token) {
          alert("Please login from ReplyCraft website first");

          window.open(
            "https://email-reply-craft.vercel.app",
            "_blank"
            )
          return;
        }

        const emailBodies = document.querySelectorAll(".a3s");

        if (!emailBodies.length) {
          alert("No email body found");

          return;
        }

        const latestEmail = emailBodies[emailBodies.length - 1];

        const text = latestEmail.innerText;

        btn.innerText = "Generating...";

        const response = await fetch(
          "https://email-replycraft.onrender.com/api/email/generate",
          {
            method: "POST",

            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify({
              emailContent: text,
              tone: "formal",
            }),
          }
        );

        const data = await response.json();

        console.log(data);

        if (!response.ok) {
          btn.innerText = "Generate Reply";

          alert(data.message || "Failed");

          return;
        }

        // FIND CURRENT REPLY BOX
        const replyBox = container.querySelector("[role='textbox']");

        if (replyBox) {
          replyBox.focus();

          document.execCommand("insertText", false, data.data);
        }

        btn.innerText = "Generate Reply";
      } catch (error) {
        console.error(error);

        btn.innerText = "Generate Reply";

        alert("Failed to generate reply");
      }
    });

    const wrapper = document.createElement("div");

    wrapper.style.marginLeft = "8px";
    wrapper.style.display = "flex";
    wrapper.style.alignItems = "center";

    wrapper.appendChild(btn);

    sendButton.parentElement.insertAdjacentElement("afterend", wrapper);
  });
}

setInterval(injectButtons, 1500);
