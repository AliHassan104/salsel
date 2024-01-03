function goInDetails() {
    window.location.href = "ticket-detail.html"
}

authenticate();

function authenticate() {
    jwtToken = JSON.parse(localStorage.getItem("token"))
    if (jwtToken == null) {
        window.location.href = "../screens/login.html"
    }
}


getAllTickets();

// function getAllTickets(){

//     https://api.salselexpress.com/api/ticket/logged-in-user?status=false

// }



function showLoader() {
    document.getElementById("loader-container").style.display = "flex";
}

function hideLoader() {
    document.getElementById("loader-container").style.display = "none";
}


var getUser;
var allTickets = [];
function getAllTickets() {

    showLoader();
    getUser = JSON.parse(localStorage.getItem("loggedInUser"))
    jwtToken = JSON.parse(localStorage.getItem("token"))

    let apiUrl = `https://api.salselexpress.com/api/ticket?status=true`;


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

    // let ajax= new XMLHttpRequest();
    // ajax.open("GET" , apiUrl);
    // ajax.setRequestHeader("content-type", "application/json");
    // ajax.setRequestHeader("Authorization", "Bearer " + jwtToken);
    // ajax.onprogress = function(){}
    // ajax.onload = function(){
    //     console.log(this.response);
    // }
    // ajax.send();
}

function showTickets() {
    debugger
    const tableBody = document.querySelector('#myTable tbody');
    // Clear existing rows
    tableBody.innerHTML = '';

    // Iterate through the array and create table rows
    allTickets.forEach(data => {
        debugger
        const row = `
            <tr onclick="goInDetails()">
                <td>${data.category}</td>
                <td scope="row">${data.Title}</td>
                <td>${data.deliveryAddress}</td>
                <td>${data.department}</td>
                <td>${data.departmentCategory}</td>
                <td>${data.destinationCity}</td>
                <td>${data.originCity}</td>
                <td>
                    <span style="color: #1572e8; display: inline-block; padding: 6px 10px; font-weight: bold; border: 1px solid #ebecec; background: #f8fcfe;">New</span>
                    <span style="color: #ed8e00; display: inline-block; padding: 6px 10px; font-weight: bold; border: 1px solid #ebecec; background: #f8fcfe;">High</span>
                </td>
                <td colspan="3" style="text-align: center;">
                    <span class="action-edit"><i class="fa-regular fa-pen-to-square"></i> Edit Ticket</span>
                    <span class="action-delete"><i class="fa-solid fa-trash-can"></i> Delete</span>
                    <span class="action-enforce-delete"><i class="fa-solid fa-delete-left"></i> Enforce Delete</span>
                </td>
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

var ticketType = []
function getApiController(token, endPoint) {
    fetch(`https://api.salselexpress.com/api${endPoint}`, {
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

            ticketType = data;
            if (ticketType != null) {
                showTicketType();
            }

        })
        .catch(error => {
            hideLoader();
            console.error('There was a problem with the fetch operation:', error);
        });

}
getTicketType();

function getTicketType() {
    jwtToken = JSON.parse(localStorage.getItem("token"))
    endPoint = '/product-field'
    getApiController(jwtToken.jwt, endPoint)
}

function showTicketType() {
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


function selectTicketType(ticketType) {
    debugger

    debugger


    switch (ticketType) {
        case "Subject":
        document.getElementById("shipmentInquiry").style.display = 'flex'
        document.getElementById("pickupRequest").style.display = 'none'

        break;

        case "Text Box":
        document.getElementById("shipmentInquiry").style.display = 'none'
        document.getElementById("pickupRequest").style.display = 'flex'
        break;

        case "Priority":
        document.getElementById("priority").style.display = 'flex'
        break;
        
        case "Attachements":
        document.getElementById("rateInquiry").style.display = 'flex'
        break;
 
    }
}

function createTicket(){
    const ticketType = document.getElementById('ticketType').value;
    const name = document.getElementById('inputPassword4').value;
    const email = document.getElementById('inputEmail4').value;
    const phone = document.getElementById('inputAddress').value;

    const entryAirwayOrReferenceNumber = document.getElementById('shipmentInquiry').querySelector('input').value;

    const shipperName = document.getElementById('pickupRequest').querySelector('input[placeholder="Shipper Name"]').value;
    const shipperContactNumber = document.getElementById('pickupRequest').querySelector('input[placeholder="Contact Number"]').value;
    const originCountryAndCity = document.getElementById('pickupRequest').querySelector('select').value;
    const pickupAddress = document.getElementById('pickupRequest').querySelector('input[placeholder="PickUp Address"]').value;
    const shipperRef = document.getElementById('pickupRequest').querySelector('input[placeholder="Shipper Ref#"]').value;
    const recipientsName = document.getElementById('pickupRequest').querySelector('input[placeholder="Recipients Name"]').value;
    const recipientsContactNumber = document.getElementById('pickupRequest').querySelector('input[placeholder="Contact Number"]').value;
    const recipientsOriginCountryAndCity = document.getElementById('pickupRequest').querySelectorAll('select')[1].value;
    const recipientsPickUpAddress = document.getElementById('pickupRequest').querySelector('input[placeholder="Recipients PickUp Address"]').value;

    const entryAirwayOrReferenceNumberRateInquiry = document.getElementById('rateInquiry').querySelector('input').value;

    const textareaValue = document.querySelector('textarea').value;

    const ticket = {
        assignedTo: "string",
        category: "string",
        createdAt: "2024-01-03T13:26:15.459Z",
        createdBy: "string",
        deliveryAddress: "string",
        department: "string",
        departmentCategory: "string",
        destinationCity: "string",
        destinationCountry: "string",
        id: 0,
        originCity: "string",
        originCountry: "string",
        pickupAddress: "string",
        pickupDate: "2024-01-03",
        pickupTime: {
          hour: "string",
          minute: "string",
          nano: 0,
          second: "string"
        },
        recipientContactNumber: "string",
        recipientName: "string",
        shipperContactNumber: "string",
        shipperName: "string",
        shipperRefNumber: "string",
        status: true,
        ticketFlag: "string",
        ticketStatus: "string"
      };
      
      console.log(ticketObject);
      
}