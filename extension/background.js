chrome.runtime.onMessageExternal.addListener(
  (request, sender, sendResponse) => {
    console.log("MESSAGE RECEIVED", request);

    if (request.type === "SET_TOKEN") {
      chrome.storage.local.set(
        {
          accessToken: request.token,
        },
        () => {
          console.log("TOKEN SAVED");
        }
      );

      sendResponse({
        success: true,
      });
    }
  }
);
