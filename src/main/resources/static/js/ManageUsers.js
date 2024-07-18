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

document.getElementById('inputSearch').onkeyup = searchFunction;

let popUpOpen = false;

document.querySelectorAll('.promote-button, .demote-button, .delete-button').forEach(button => {
   button.addEventListener("click", evt => {
       evt.preventDefault();

       if (popUpOpen) {
           return;
       }

       popUpOpen = true;

       const usernameField = button.closest('form').querySelector('.username-field');
       const username = usernameField.value;
       let confirmMessage = "";
       if (button.classList.contains("promote-button")) {
            confirmMessage = "Are you sure you want to promote USER: " + username + " to ADMIN?"
       } else if (button.classList.contains("demote-button")) {
            confirmMessage = "Are you sure you want to demote ADMIN: " + username + " to USER?"
       } else if (button.classList.contains("delete-button")) {
            confirmMessage = "Are you sure you want to delete USER: " + username + "?"
       }

       const confirmationPopup = document.createElement('div');
       confirmationPopup.classList.add('confirmation-popup', 'bg-blur', 'container');

       const messageElement = document.createElement('p');
       messageElement.textContent = confirmMessage;
       confirmationPopup.appendChild(messageElement);

       const buttonContainer = document.createElement('div');
       buttonContainer.classList.add('button-container');

       const confirmButton = document.createElement('button');
       confirmButton.textContent = "Confirm";
       confirmButton.classList.add('admin-button', 'btn', 'btn-danger', 'mx-2')
       confirmButton.addEventListener("click", () => {
            button.parentElement.submit();
            confirmationPopup.remove();
            popUpOpen = false;
       });

       const cancelButton = document.createElement('button');
       cancelButton.textContent = "Cancel";
       cancelButton.classList.add('admin-button', 'btn', 'btn-primary', 'mx-2');
       cancelButton.addEventListener("click", () => {
          confirmationPopup.remove();
           popUpOpen = false;
       });

       buttonContainer.appendChild(confirmButton);
       buttonContainer.appendChild(cancelButton);
       confirmationPopup.appendChild(buttonContainer);

       document.body.appendChild(confirmationPopup);
   })
});
