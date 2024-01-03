/*!
=========================================================
* Creative Studio Landing page
=========================================================

* Copyright: 2019 DevCRUD (https://devcrud.com)
* Licensed: (https://devcrud.com/licenses)
* Coded by www.devcrud.com

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/

// smooth scroll
$(document).ready(function () {
    $(".navbar .nav-link").on('click', function (event) {

        if (this.hash !== "") {

            event.preventDefault();

            var hash = this.hash;

            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 700, function () {
                window.location.hash = hash;
            });
        }
    });
});

function goToService(pages) {
    if (pages == 'doorToDoor') {
        window.location.href = 'services/door to door service.html'
    }
    else if (pages == "seaCargo") {
        window.location.href = 'services/seacargo.html';
    }
    else if (pages == "truckRoadFreight") {
        window.location.href = 'services/truckloadfreight.html';
    }
    else if (pages == "roadFreight") {
        window.location.href = 'services/roadfreight.html';

    }
    else if (pages == "ecommerce") {
        window.location.href = 'services/ecomercedelivery.html';
    }
    else if (pages == "specialDelivery") {
        window.location.href = 'services/specialdelivery.html';

    }
    else if (pages == "coldChain") {
        window.location.href = 'services/coldchain.html';

    }
    else if (pages == "threePl") {
        window.location.href = 'services/3pl.html';

    }
    else if (pages == "repairReturn") {
        window.location.href = 'services/repairreturn.html';


    }

    switch (pages) {

        // case "truckRoadFreight":
        // window.location.href = 'services/truckloadfreight.html';


        // case "doorToDoor":
        // window.location.href = 'services/door to door service.html';

        // case "seaCargo":
        // window.location.href = 'services/seacargo.html';

        // case "roadFreight":
        // window.location.href = 'services/roadfreight.html';

        // case "ecommerce":
        // window.location.href = 'services/ecomercedelivery.html';

        // case "specialDelivery":
        // window.location.href = 'services/specialdelivery.html';

        // case "coldChain":
        // window.location.href = 'services/coldchain.html';

        // case "threePl":
        // window.location.href = 'services/3pl.html';

        // case "repairReturn":
        // window.location.href = 'services/repairreturn.html';

    }

    function handleIntersection(entries, observer) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                // Add the animated class to trigger the animation
                entry.target.classList.add('animated', 'animate__bounce');
                // Stop observing after the animation is triggered
                observer.unobserve(entry.target);
            }
        });
    }

    // Create an intersection observer
    const observer = new IntersectionObserver(handleIntersection, { threshold: 0.5 });

    // Target the element to observe
    const targetElement = document.querySelector('.overlay');

    // Start observing the target element
    observer.observe(targetElement);
}


function goToServiceDetail(service, inService) {
    debugger
    if (inService == undefined) {
        if (service == "doorToDoor") {
            window.location.href = 'services/door to door service.html';
        } else if (service == "seaCargo") {
            window.location.href = 'services/seacargo.html';
        } else if (service == "roadFreight") {
            window.location.href = 'services/roadfreight.html';
        } else if (service == "ecommerce") {
            window.location.href = 'services/ecomercedelivery.html';
        } else if (service == "specialDelivery") {
            window.location.href = 'services/specialdelivery.html';
        } else if (service == "coldChain") {
            window.location.href = 'services/coldchain.html';
        } else if (service == "threePl") {
            window.location.href = 'services/3pl.html';
        } else if (service == "return") {
            window.location.href = 'services/repairreturn.html';
        } else if (service == "truckRoadFreight") {
            window.location.href = 'services/truckloadfreight.html';
        }

    } else {
        if (service == "doorToDoor") {
            window.location.href = '../services/door to door service.html';
        } else if (service == "seaCargo") {
            window.location.href = '../services/seacargo.html';
        } else if (service == "roadFreight") {
            window.location.href = '../services/roadfreight.html';
        } else if (service == "ecommerce") {
            window.location.href = '../services/ecomercedelivery.html';
        } else if (service == "specialDelivery") {
            window.location.href = '../services/specialdelivery.html';
        } else if (service == "coldChain") {
            window.location.href = '../services/coldchain.html';
        } else if (service == "threePl") {
            window.location.href = '../services/3pl.html';
        } else if (service == "return") {
            window.location.href = '../services/repairreturn.html';
        } else if (service == "truckRoadFreight") {
            window.location.href = '../services/truckloadfreight.html';
        }
    }

  



    // function hideGoogleTranslateToolbar() {
    //     var observer = new MutationObserver(function (mutations) {
    //         mutations.forEach(function (mutation) {
    //             if (mutation.addedNodes) {
    //                 mutation.addedNodes.forEach(function (node) {
    //                     if (node.className === 'goog-te-banner-frame') {
    //                         node.style.display = 'none';
    //                     }
    //                 });
    //             }
    //         });
    //     });

    //     var target = document.documentElement || document.body;
    //     var config = { attributes: false, childList: true, subtree: true };

    //     observer.observe(target, config);
    // }

    // function updateSelectedLanguageFlag(languageCode) {
    //     debugger
    //     var flagImg = document.getElementById('selected_language_flag');
    //     var flagPath = 'path/to/' + languageCode + '_flag.png'; // Adjust the path based on your directory structure

    //     if (flagImg) {
    //         flagImg.src = flagPath;
    //     }

    //     console.log(languageCode);
    // }

    // function googleTranslateOnLoad() {
    //     setTimeout(function () {
    //         hideGoogleTranslateToolbar();

    //         // Set up a listener for language change events
    //         var observer = new MutationObserver(function (mutations) {
    //             mutations.forEach(function (mutation) {
    //                 if (mutation.type === 'attributes' && mutation.attributeName === 'lang') {
    //                     var selectedLanguageCode = mutation.target.getAttribute('lang');
    //                     updateSelectedLanguageFlag(selectedLanguageCode);
    //                 }
    //             });
    //         });

    //         // Watch for changes in the lang attribute of the body element
    //         var target = document.documentElement || document.body;
    //         var config = { attributes: true, childList: false, subtree: false };

    //         observer.observe(target, config);

    //         // Set the flag for the initial language
    //         var initialLanguageCode = document.documentElement.lang;
    //         updateSelectedLanguageFlag(initialLanguageCode);
    //     }, 1000); // Adjust the delay as needed
    // }


    function googleTranslateElementInit() {
        new google.translate.TranslateElement({
            pageLanguage: 'en',  // Change this to the default language of your website
            includedLanguages: 'en,es,fr,de,ar',  // Add the languages you want to support
            layout: google.translate.TranslateElement.InlineLayout.SIMPLE,
            autoDisplay: false,
            suppressInitialPostRender: false  // Add this option to suppress the initial toolbar display
        }, 'google_translate_element');
    }

    function hideGoogleTranslateToolbar() {
        var googleTranslateFrame = document.querySelector('.goog-te-banner-frame');
        var googleTranslateParent = googleTranslateFrame ? googleTranslateFrame.parentNode : null;

        if (googleTranslateParent) {
            googleTranslateParent.style.display = 'none';
        }
    }

    function updateFlagImage(languageCode) {
        console.log('Updating flag for language:', languageCode);
        var flagMappings = {
            'en': 'english.png',
            'es': 'spanish.png',
            'fr': 'french.png',
            'de': 'german.png',
            'ar': 'assets/imgs/arabic.png'
            // Add more mappings as needed
        };

        var flagFileName = flagMappings[languageCode];
        if (flagFileName) {
            var flagPath = 'assets/imgs/' + flagFileName;
            var flagImage = document.getElementById('selected_language_flag');

            if (flagImage) {
                flagImage.src = flagPath;
            }
        }
    }

    // Trigger the function after the translation is complete
    function googleTranslateOnLoad() {
        setTimeout(function () {
            hideGoogleTranslateToolbar();

            var translateElement = document.getElementById('google_translate_element');
            if (translateElement) {
                translateElement.addEventListener('change', function () {
                    var selectedLanguage = translateElement.querySelector(':checked').value;
                    updateFlagImage(selectedLanguage);
                });
            }
        }, 500);
    }

}


var plusIcon = document.querySelector("#plus");
var socialIcons = document.querySelector("#menu");
var toggle = 0;
function expand() {
    if (toggle == 0) {
        plusIcon.style.transform = "rotate(360deg)";
        socialIcons.style.transform = "scale(2)";
        toggle = 1;
    } else {
        plusIcon.style.transform = "rotate(0deg)";
        socialIcons.style.transform = "scale(0)";
        toggle = 0;
    }
}