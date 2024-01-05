async function getApiController(token, endPoint) {
    try {
        const response = await fetch(`https://api.salassilexpress.com/api${endPoint}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
                // Add any additional headers if needed
            }
        });

        hideLoader();

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        hideLoader();
        console.error('There was a problem with the fetch operation:', error);
        return null;
    }
}

async function postApiController(token, endPoint, payload) {
    try {
        showLoader();

        const response = await fetch(`https://api.salassilexpress.com/api/${endPoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token.jwt}`
                // Add any additional headers if needed
            },
            body: JSON.stringify(payload)
        });

        hideLoader();

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        return data;
        // You can perform additional actions with the data if needed

    } catch (error) {
        hideLoader();
        console.error('There was a problem with the fetch operation:', error);
    }
}


async function putApiController(token, endPoint, payload) {
    showLoader();

    try {
        const response = await fetch(`https://api.salassilexpress.com/api/${endPoint}`, {
            method: 'PUT', // Change method to 'PUT'
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token.jwt}`
                // Add any additional headers if needed
            },
            body: JSON.stringify(payload)
            // If you have data to send in the request body, include it here
            // body: JSON.stringify({ key1: 'value1', key2: 'value2' })
        });

        hideLoader();

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        return data
        // Process the response data as needed

    } catch (error) {
        hideLoader();
        console.error('There was a problem with the fetch operation:', error);
    }
}


async function createTicket() {

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const textareaValue = document.getElementById('textarea').value;
    const ticketType = document.getElementById('ticketType').value;

    loggedInUser = JSON.parse(localStorage.getItem("loggedInUser"))

    const urlParams = new URLSearchParams(window.location.search);
    const ticketId = urlParams.get('id');

    const endPoint = `/ticket/${ticketId}`

    if (ticketId) {
        try {
            var getTicketById = await getApiController(jwtToken.jwt, endPoint);

            if (getTicketById) {

                const ticket = {
                    assignedTo: getTicketById.assignedTo,
                    shipperName: getTicketById.shipperName,
                    shipperContactNumber: getTicketById.shipperContactNumber,
                    pickupAddress: getTicketById.pickupAddress,
                    shipperRefNumber: getTicketById.shipperRefNumber,
                    originCountry: getTicketById.originCountry,
                    originCity: getTicketById.originCity,
                    recipientName: getTicketById.recipientName,
                    recipientContactNumber: getTicketById.recipientContactNumber,
                    destinationCountry: getTicketById.destinationCountry,
                    destinationCity: getTicketById.destinationCity,
                    deliveryAddress: getTicketById.deliveryAddress,
                    pickupDate: getTicketById.pickupDate,
                    pickupTime: getTicketById.pickupTime,
                    category: getTicketById.category,
                    ticketStatus: getTicketById.ticketStatus,
                    ticketFlag: getTicketById.ticketFlag,
                    department: getTicketById.department,
                    departmentCategory: getTicketById.departmentCategory,
                    deliveryStreetName: getTicketById.deliveryStreetName,
                    deliveryDistrict: getTicketById.deliveryDistrict,
                    pickupStreetName: getTicketById.pickupStreetName,
                    pickupDistrict: getTicketById.pickupDistrict,
                    ticketType: ticketType,
                    weight: getTicketById.weight,
                    name: name,
                    email: email,
                    phone: phone,
                    textarea: textareaValue,
                    airwayNumber: getTicketById.airwayNumber,
                    createdBy: loggedInUser
                }
                jwtToken = JSON.parse(localStorage.getItem("token"))

                var updatedTicket = await putApiController(jwtToken, `ticket/${getTicketById.id}`, ticket)
                if(updatedTicket != null){
                    window.location.href = "ticket.html"
                }
            }
        } catch (error) {
            console.error('Error getting ticket data:', error);
        }
    } else {
        const ticket = {
            assignedTo: null,
            shipperName: null,
            shipperContactNumber: null,
            pickupAddress: null,
            shipperRefNumber: null,
            originCountry: null,
            originCity: null,
            recipientName: null,
            recipientContactNumber: null,
            destinationCountry: null,
            destinationCity: null,
            deliveryAddress: null,
            pickupDate: null,
            pickupTime: null,
            category: null,
            ticketStatus: null,
            ticketFlag: null,
            department: null,
            departmentCategory: null,
            deliveryStreetName: null,
            deliveryDistrict: null,
            pickupStreetName: null,
            pickupDistrict: null,
            ticketType: ticketType,
            weight: null,
            name: name,
            email: email,
            phone: phone,
            textarea: textareaValue,
            airwayNumber: null,
            createdBy: loggedInUser
        }
        jwtToken = JSON.parse(localStorage.getItem("token"))

        var createTicket = await postApiController(jwtToken, `ticket`, ticket)
        if(createTicket != null){
            window.location.href = "ticket.html"
        }
    }


}

async function getTicket() {
    const urlParams = new URLSearchParams(window.location.search);
    const ticketId = urlParams.get('id');
    if (ticketId) {
        document.getElementById("submit-button").innerHTML = "Update"
    }

    jwtToken = JSON.parse(localStorage.getItem("token"))
    let endPoint = `/ticket/${ticketId}`;

    if (ticketId) {


        try {
            var getTicketById = await getApiController(jwtToken.jwt, endPoint);

            if (getTicketById) {
                document.getElementById('name').value = getTicketById.name;
                document.getElementById('email').value = getTicketById.email;
                document.getElementById('phone').value = getTicketById.phone;
                document.querySelector('textarea').value = getTicketById.textarea;

                setTimeout(() => {
                    updateTicketTypeDropdown(getTicketById.ticketType);
                }, 100);
                // const ticketType = document.getElementById('ticketType');

                // for (let i = 0; i < ticketType.options.length; i++) {
                //     const option = ticketType.options[i];

                //     if (option.value === getTicketById.ticketType) {
                //         option.selected = true;
                //         break;
                //     }
                // }

                // console.log(getTicketById);
            }
        } catch (error) {
            console.error('Error getting ticket data:', error);
        }
    }

}

function updateTicketTypeDropdown(selectedValue) {
    const ticketType = document.getElementById('ticketType');

    for (let i = 0; i < ticketType.options.length; i++) {
        const option = ticketType.options[i];

        if (option.value === selectedValue) {
            option.selected = true;
            break;
        }
    }
}


getTicket();
