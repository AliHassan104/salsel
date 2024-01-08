authenticate();

function authenticate() {
    jwtToken = JSON.parse(localStorage.getItem("token"))
    if (jwtToken == null) {
        window.location.href = "../screens/login.html"
    }
}


getAllTickets();

function showLoader() {
    document.getElementById("loader-container").style.display = "flex";
}

function hideLoader() {
    document.getElementById("loader-container").style.display = "none";
}


var getUser;
var allTickets = [];
function getAllTickets() {
    debugger

    showLoader();
    getUser = JSON.parse(localStorage.getItem("loggedInUser"))
    jwtToken = JSON.parse(localStorage.getItem("token"))

    let apiUrl = `https://api.salassilexpress.com/api/ticket/logged-in-user?status=true`;


    fetch(apiUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken.jwt}`
            // Add any additional headers if needed
        }
    })
        .then(response => {
            hideLoader();
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Handle the response data
            // You can redirect the user or perform any other action based on the response
            allTickets = data;
            console.log(allTickets);
            showTickets();
        })
        .catch(error => {
            hideLoader();
            console.error('There was a problem with the fetch operation:', error);
        });
}

function showTickets() {
    const tableBody = document.querySelector('#myTable tbody');
    // Clear existing rows
    tableBody.innerHTML = '';

    // Iterate through the array and create table rows
    allTickets.forEach(data => {
        const row = `
        <tr>
        <td style="text-align: center;">
                <span class="action-edit" onclick="edit(${data.id})"><i class="fa-regular fa-pen-to-square"></i></span>
                <span class="action-delete"><i class="fa-solid fa-trash-can"></i></span>
                <span class="action-enforce-delete" onclick="deleteTicket(${data.id})"><i class="fa-solid fa-delete-left"></i></span>
            </td>
            <td>${data.createdAt}</td>
            <td scope="row">${data.ticketType}</td>
            <td>${data.ticketStatus}</td>
            <td>${data.ticketFlag}</td>
            <td>${data.category}</td>
            <td>${data.assignedTo}</td>
            <td>${data.departmentCategory}</td>
            
        </tr>`;

        // Append the row to the table body
        tableBody.innerHTML += row;
    });
}


function goIntoTicket(token) {
    if (token.jwt) {
        window.location.href = "../TicketPortal/ticket.html"
    }
}

function logOut() {
    localStorage.removeItem("token");
    window.location.href = "../screens/login.html"
}

// var ticketType = []
function getApiController(token, endPoint) {
    return new Promise((resolve, reject) => {
        fetch(`https://api.salassilexpress.com/api${endPoint}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
                // Add any additional headers if needed
            }
        })
            .then(response => {
                hideLoader();
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                resolve(data);
            })
            .catch(error => {
                hideLoader();
                console.error('There was a problem with the fetch operation:', error);
                reject(error);
            });
    });
}



async function getTicketType() {
    try {
        jwtToken = JSON.parse(localStorage.getItem("token"))
        endPoint = '/product-field'
        
        var ticketType = await getApiController(jwtToken.jwt, endPoint)
        if (ticketType) {
            showTicketType(ticketType)
        }
    } catch (error) {
        console.error('Error getting ticket type data:', error);
    }
}

getTicketType();

function showTicketType(ticketType) {
    var ticketTypeName = []
    ticketType.forEach(element => {
        if (element.name == 'Ticket Type') {
            element.productFieldValuesList.forEach(el => {
                ticketTypeName.push(el.name)
            })
        }
    });

    const dropdown = document.querySelector('#form #ticketType');
    // Clear existing rows
    // dropdown.innerHTML = '';
    ticketTypeName.forEach(el => {
        const opt = `
        
        <option style="color: black;" >${el}</option>
        `
        dropdown.innerHTML += opt;
    });
    dropdown.addEventListener('change', function () {
        selectTicketType(dropdown.value);
    });
}

var selectedTicketType

function selectTicketType(ticketType) {
    selectedTicketType = ticketType

}

function edit(ticketId){
    window.location.href = `create-ticket.html?id=${ticketId}`;
}

async function deleteApiController(token, endPoint) {
    showLoader();

    try {
        const response = await fetch(`https://api.salassilexpress.com/api/${endPoint}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token.jwt}`
                // Add any additional headers if needed
            },
        });

        hideLoader();

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        return data;
        // Process the response data as needed

    } catch (error) {
        hideLoader();
        console.error('There was a problem with the fetch operation:', error);
    }
}


async function deleteTicket(ticketId){
    jwtToken = JSON.parse(localStorage.getItem("token"))
    const endPoint = `ticket/${ticketId}`
    debugger
    var ticketDelete = await deleteApiController(jwtToken , endPoint)
    getAllTickets()

}

var allTickets = [];
function getAllTickets() {

    showLoader();
    getUser = JSON.parse(localStorage.getItem("loggedInUser"))
    jwtToken = JSON.parse(localStorage.getItem("token"))

    let apiUrl = `https://api.salassilexpress.com/api/ticket/logged-in-user?status=true`;


    fetch(apiUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwtToken.jwt}`
            // Add any additional headers if needed
        }
    })
        .then(response => {
            hideLoader();
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Handle the response data
            // You can redirect the user or perform any other action based on the response
            allTickets = data;
            const tableBody = document.querySelector('#myTable tbody');
            // Clear existing rows
            tableBody.innerHTML = '';
        
            // Iterate through the array and create table rows
            allTickets.forEach(data => {
                const row = `
                <tr>
                <td style="text-align: center;">
                        <span class="action-edit" onclick="edit(${data.id})"><i class="fa-regular fa-pen-to-square"></i></span>
                        <span class="action-delete"><i class="fa-solid fa-trash-can"></i></span>
                        <span class="action-enforce-delete" onclick="deleteTicket(${data.id})"><i class="fa-solid fa-delete-left"></i></span>
                    </td>
                    <td>${data.createdAt}</td>
                    <td scope="row">${data.ticketType}</td>
                    <td>${data.ticketStatus}</td>
                    <td>${data.ticketFlag}</td>
                    <td>${data.category}</td>
                    <td>${data.assignedTo}</td>
                    <td>${data.departmentCategory}</td>
                    
                </tr>`;
        
                // Append the row to the table body
                tableBody.innerHTML += row;
            });
        })
        .catch(error => {
            hideLoader();
            console.error('There was a problem with the fetch operation:', error);
        });
}