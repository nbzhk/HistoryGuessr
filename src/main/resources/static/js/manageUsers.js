function searchFunction() {
    let input, filter, tr, td, txtValue;

    input = document.getElementById("inputSearch");
    filter = input.value.toLowerCase();
    tr = document.getElementsByTagName("tr");

    for (let i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toLowerCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

document.querySelectorAll('.promote-button, .demote-button, .delete-button').forEach(button => {
   button.addEventListener("click", evt => {
       evt.preventDefault();
       const username = document.querySelector('.username-field').value;
       let confirmMessage = "";
       if (button.classList.contains("promote-button")) {
            confirmMessage = "Are you sure you want to promote USER: " + username + " to ADMIN?"
       } else if (button.classList.contains("demote-button")) {
            confirmMessage = "Are you sure you want to demote ADMIN: " + username + " to USER?"
       } else if (button.classList.contains("delete-button")) {
            confirmMessage = "Are you sure you want to delete USER: " + username + "?"
       }
       if (confirm(confirmMessage)){
           button.parentElement.submit();
       }
   })
});
