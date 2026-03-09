document.getElementById("fetchBtn").addEventListener("click", fetchRepos);

async function fetchRepos() {
    const username = document.getElementById("username").value.trim();
    const repoList = document.getElementById("repoList");
    const errorDiv = document.getElementById("error");

    repoList.innerHTML = "";
    errorDiv.innerText = "";

    if (!username) {
        errorDiv.innerText = "Please enter a GitHub username.";
        return;
    }

    try {
        const response = await fetch(`/api/recommend/links/${username}`);
        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }

        const repos = await response.json();

        if (!Array.isArray(repos) || repos.length === 0) {
            repoList.innerHTML = "<li>No recommended repos found for this user.</li>";
            return;
        }

        repos.forEach(link => {
            const li = document.createElement("li");
            const a = document.createElement("a");
            a.href = link;
            a.target = "_blank";
            a.innerText = link;
            li.appendChild(a);
            repoList.appendChild(li);
        });

    } catch (err) {
        errorDiv.innerText = err.message;
    }
}