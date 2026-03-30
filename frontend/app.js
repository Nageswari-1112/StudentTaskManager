const API = "http://localhost:8080/tasks";

async function loadTasks() {
    const res = await fetch(API);
    const data = await res.text();

    document.getElementById("list").innerHTML =
        data.split("\n")
        .filter(t => t)
        .map(t => {
            const [id, name, date, completed] = t.split(",");

            return `
                <p>
                    <input type="checkbox"
                        ${completed === "true" ? "checked" : ""}
                        onchange="markComplete('${id}')"
                    >

                    <b>${name}</b><br>
                    Due: ${date}<br>
                    Status: ${completed === "true" ? "✅ Done" : "⏳ Pending"}
                </p>
            `;
        })
        .join("");
}

async function addTask() {
    const task = document.getElementById("task").value;
    const date = document.getElementById("date").value;

    if (!task || !date) {
        alert("Please enter task and date");
        return;
    }

    const data = `${Date.now()},${task},${date},false`;

    await fetch(API, {
        method: "POST",
        body: data
    });

    document.getElementById("task").value = "";
    document.getElementById("date").value = "";

    loadTasks();
}


async function markComplete(id) {
    console.log("Clicked ID:", id);  // 👈 ADD THIS

    await fetch(`http://localhost:8080/complete?id=${id}`);
    loadTasks();
}
loadTasks();