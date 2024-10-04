document.addEventListener('DOMContentLoaded', function () {
    fetchCurrentUser();
    fetchUsers();
    loadRoles();
    setupCloseButtons();
});

//Get currentUser
function fetchCurrentUser() {
    fetch('/admin/currentUser')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch current user info');
            }
            return response.json();
        })
        .then(user => {
            const emailSpan = document.getElementById('currentUserEmail');
            const rolesSpan = document.getElementById('currentUserRoles');
            emailSpan.textContent = user.email;
            rolesSpan.textContent = user.roles.map(role => role.name).join(' ');
        })
        .catch(error => {
            console.error('Error fetching current user info:', error);
        });
}

// Get all Users
function fetchUsers() {
    fetch('/admin/users')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch users');
            }
            return response.json();
        })
        .then(response => {
            console.log('Users fetched:', response);
            const tableBody = document.getElementById('users-table-body');
            tableBody.innerHTML = '';
            response.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.firstname}</td>
                    <td>${user.lastname}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(role => role.name).join(', ')}</td> 
                    <td><button class="btn btn-info" onclick="openEditUserForm(${user.id})">Edit</button></td>
                    <td><button class="btn btn-danger" onclick="openDeleteUserForm(${user.id})">Delete</button></td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching users:', error);
        });
}

function loadRoles() {
    fetch('/admin/roles')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch roles');
            }
            return response.json();
        })
        .then(roles => {
            console.log('Roles fetched:', roles);
            const roleSelect = document.getElementById('roleAdd');
            const editRoleSelect = document.getElementById('roleEdit');
            const delRoleSelect = document.getElementById('roleDel')
            roleSelect.innerHTML = '';
            editRoleSelect.innerHTML = '';
            delRoleSelect.innerHTML = '';
            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.id;
                option.text = role.name;
                roleSelect.appendChild(option);
                const editOption = document.createElement('option');
                editOption.value = role.id;
                editOption.text = role.name;
                editRoleSelect.appendChild(editOption);
                const delOption = document.createElement('option');
                delOption.value = role.id;
                delOption.text = role.name;
                delRoleSelect.appendChild(delOption);
            });
        })
        .catch(error => {
            console.error('Error loading roles:', error);
            alert('Ошибка при загрузке ролей');
        });
}

// EventListener new-user-form
document.getElementById('new-user-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const formData = new FormData(this);
    const rolesSelected = Array.from(document.getElementById('roleAdd').selectedOptions).map(option => ({
        id: parseInt(option.value, 10)
    }));
    const user = {
        firstname: formData.get('firstname'),
        lastname: formData.get('lastname'),
        age: parseInt(formData.get('age'), 10),
        email: formData.get('email'),
        password: formData.get('password'),
        roles: rolesSelected
    };
    fetch('/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (response.ok) {
                fetchUsers();
                this.reset();
                document.getElementById("show-users-table").click();
            } else {
                return response.json().then(data => {
                    throw new Error(data.message || 'Не удалось создать пользователя');
                });
            }
        })
        .catch(error => {
            console.error('Error: ' + error.message);
        });
});


function openEditUserForm(userId) {
    console.log('Opening edit modal for user ID:', userId);
    fetch(`/admin/users/${userId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user');
            }
            return response.json();
        })
        .then(user => {
            console.log('User fetched for edit:', user);
            document.getElementById('idEdit').value = user.id;
            document.getElementById('firstnameEdit').value = user.firstname;
            document.getElementById('lastnameEdit').value = user.lastname;
            document.getElementById('ageEdit').value = user.age;
            document.getElementById('emailEdit').value = user.email;
            const editRolesSelect = document.getElementById('roleEdit');
            Array.from(editRolesSelect.options).forEach(option => {
                option.selected = user.roles.some(role => role.id === parseInt(option.value, 10));
            });
            openModal('editUserPopup');
        })
        .catch(error => {
            console.error('Error fetching user:', error);
        });
}

//EventListener editUserForm
document.getElementById('editUserForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const formData = new FormData(this);
    const rolesSelected = Array.from(document.getElementById('roleEdit').selectedOptions).map(option => ({
        id: parseInt(option.value, 10)
    }));
    const user = {
        id: formData.get('id'),
        firstname: formData.get('firstname'),
        lastname: formData.get('lastname'),
        age: parseInt(formData.get('age'), 10),
        email: formData.get('email'),
        password: formData.get('password'),
        roles: rolesSelected
    };
    fetch(`/admin/users/${user.id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (response.ok) {
                fetchUsers();
                closeModal('editUserPopup');
            } else {
                return response.json().then(data => {
                    console.error('Error update user:', data);
                });
            }
        })
        .catch(error => {
            console.error('Error update user:', error);
        });
});

function openDeleteUserForm(userId) {
    fetch(`/admin/users/${userId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user');
            }
            return response.json();
        })
        .then(user => {
            document.getElementById('idDel').value = user.id;
            document.getElementById('firstnameDel').value = user.firstname;
            document.getElementById('lastnameDel').value = user.lastname;
            document.getElementById('ageDel').value = user.age;
            document.getElementById('emailDel').value = user.email;
            const deleteRolesSelect = document.getElementById('roleDel');
            Array.from(deleteRolesSelect.options).forEach(option => {
                option.selected = user.roles.some(role => role.id === parseInt(option.value, 10));
            });
            openModal('deleteUserPopup');
        })
        .catch(error => {
            console.error('Error fetch user:', error);
        });

}

//EventListener deleteUserForm
document.getElementById('deleteUserForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const formData = new FormData(this);
    const userId = formData.get('id');
    fetch(`/admin/users/${userId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                fetchUsers();
                closeModal('deleteUserPopup');
            } else {
                return response.json().then(data => {
                    throw new Error(data.message);
                });
            }
        })
        .catch(error => {
            console.error('Error delete user:', error);
        });
});


function openModal(modalId) {
    const modal = document.getElementById(modalId);
    const overlay = document.getElementById('overlay');
    if (modal && overlay) {
        modal.style.display = 'block';
        overlay.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    const overlay = document.getElementById('overlay');
    if (modal && overlay) {
        modal.style.display = 'none';
        overlay.style.display = 'none';
        document.body.style.overflow = 'auto';
    }
}

function setupCloseButtons() {
    const closeButtons = document.querySelectorAll('[id^="closePopup"]');
    closeButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            const modalId = this.getAttribute('data-modal');
            if (modalId) {
                closeModal(modalId);
            }
        });
    });

    const overlay = document.getElementById('overlay');
    overlay.addEventListener('click', function () {
        const modals = document.querySelectorAll('.popup');
        modals.forEach(modal => {
            modal.style.display = 'none';
        });
        this.style.display = 'none';
        document.body.style.overflow = 'auto';
    });
}