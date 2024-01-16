async function getApiController(token, endPoint) {
    showLoader();
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
    showLoader();

    try {
        const formData = new FormData();
        formData.append('ticketDto', new Blob([JSON.stringify(payload.ticketDto)], { type: 'application/json' }));
        // formData.append('file', payload.file, `${selectedFileName}.pdf`);
        for (let i = 0; i < payload.file.length; i++) {
            formData.append('files', payload.file[i]);
        }

        const response = await fetch(`https://api.salassilexpress.com/api/${endPoint}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token.jwt}`

                // Add any additional headers if needed
            },
            body: formData,
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
    }
}


async function putApiController(token, endPoint, payload) {
    showLoader();

    try {
        const formData = new FormData();
        formData.append('ticketDto', new Blob([JSON.stringify(payload.ticketDto)], { type: 'application/json' }));
        // formData.append('file', payload.file, `${selectedFileNames}`);
        for (let i = 0; i < payload.file.length; i++) {
            formData.append('files', payload.file[i]);
        }

        const response = await fetch(`https://api.salassilexpress.com/api/${endPoint}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token.jwt}`
                // Add any additional headers if needed
            },
            body: formData,
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
    }
}


async function createTicket() {

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const textareaValue = document.getElementById('textarea').value;
    const ticketType = document.getElementById('ticketType').value;
    const fileInput = document.getElementById('yourFileInputId');
    const file = fileInput.files;

    loggedInUser = JSON.parse(localStorage.getItem("loggedInUser"))

    const urlParams = new URLSearchParams(window.location.search);
    const ticketId = urlParams.get('id');

    const endPoint = `/ticket/${ticketId}`
    if (!name || !email || !ticketType) {
        // Show the error toast
        
        var errorToast = new bootstrap.Toast(document.getElementById('errorToast'));
        errorToast.show();
        return;
    }

    var errorToast = new bootstrap.Toast(document.getElementById('errorToast'), {
        delay: 5000 // Set the delay to 5000 milliseconds (5 seconds)
    });
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

                const payload = {
                    ticketDto: ticket,
                    file: file,
                };
                var updatedTicket = await putApiController(jwtToken, `ticket/${getTicketById.id}/filenames?fileNames=${selectedFileNames.join(',')}`, payload)

                if (updatedTicket != null) {
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

        const payload = {
            ticketDto: ticket,
            file: file,
        };

        var createTicket = await postApiController(jwtToken, `ticket`, payload)
        if (createTicket != null) {
            window.location.href = "ticket.html"
        }
    }


}
getTicket();
async function getTicket() {
    const urlParams = new URLSearchParams(window.location.search);
    const ticketId = urlParams.get('id');
    if (ticketId) {
        document.getElementById("submit-button").innerHTML = "Update"
        document.getElementById("create-header").innerHTML = "Update Ticket"
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
                const attachementEndPoint = `/file/${getTicketById.ticketUrl}`;
                // const getAttachement = await getApiController(jwtToken.jwt, attachementEndPoint);

                // const blob = new Blob([getAttachement], { type: 'application/pdf' });

                // const fileInput = document.getElementById('yourFileInputId');
                // const fileList = new DataTransfer();
                
                // const fileName = getTicketById.ticketUrl.split('/').pop(); // Extracting the file name
                
                // fileList.items.add(new File([blob], fileName, { type: 'application/pdf' }));
                // fileInput.files = fileList.files;
                
                // fileInput.dispatchEvent(new Event('change'));
                displayAttachments(getTicketById.attachments);

                setTimeout(() => {
                    updateTicketTypeDropdown(getTicketById.ticketType);
                }, 100);
            }
        } catch (error) {
            console.error('Error getting ticket data:', error);
        }
    }

}

function displayAttachments(attachments) {
    const fileInput = document.getElementById('yourFileInputId');
    const fileList = new DataTransfer();

    if (attachments && attachments.length > 0) {
        attachments.forEach(attachment => {
            const blob = new Blob([attachment.filePath], { type: 'application/pdf' });
            const fileName = attachment.filePath.split('/').pop();

            fileList.items.add(new File([blob], fileName, { type: 'application/pdf' }));
        });
    }

    fileInput.files = fileList.files;
    fileInput.dispatchEvent(new Event('change'));
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



let selectedFileNames = [];

function onFileChange(event) {
    const fileInput = event.target;
    const fileNameDisplay = document.getElementById('fileNameDisplay');

    selectedFileNames = []; // Clear the array before updating with new files

    if (fileInput.files.length > 0) {
        for (let i = 0; i < fileInput.files.length; i++) {
            selectedFileNames.push(fileInput.files[i].name);
        }

        fileNameDisplay.innerText = selectedFileNames.join(', ');
    } else {
        fileNameDisplay.innerText = 'No file selected';
    }
}
