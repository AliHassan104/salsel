package com.salsel.controller;

import com.salsel.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class PricingController {
    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/pricing")
    public ResponseEntity<Double> getPricing(@RequestParam(value = "fromCountry") String fromCountry,
                                             @RequestParam(value = "toCountry") String toCountry,
                                             @RequestParam(value = "product") String product,
                                             @RequestParam(value = "productWeight") Double productWeight){
        Double price = pricingService.getPrice(fromCountry,toCountry,product,productWeight);
        return ResponseEntity.ok(price);
    }
}


//    INSERT INTO `salsel`.`product_field` (`created_at`, `name`, `sequence`, `status`, `type`) VALUES ('2024-01-17', 'Country', '18', 'Active', 'MULTIDROPDOWN');
//    INSERT INTO `salsel`.`product_field` (`created_at`, `name`, `sequence`, `status`, `type`) VALUES ('2024-01-17', 'Products', '16', 'Active', 'MULTIDROPDOWN');


//    INSERT INTO salsel.product_field_values (name, status, product_field_id)
//        VALUES
//        ('Afghanistan', 'Active', 18),
//        ('Åland (Finland)', 'Active', 18),
//        ('Albania', 'Active', 18),
//        ('Algeria', 'Active', 18),
//        ('American Samoa (US)', 'Active', 18),
//        ('Andorra', 'Active', 18),
//        ('Angola', 'Active', 18),
//        ('Anguilla (BOT)', 'Active', 18),
//        ('Antigua and Barbuda', 'Active', 18),
//        ('Argentina', 'Active', 18),
//        ('Armenia', 'Active', 18),
//        ('Artsakh', 'Active', 18),
//        ('Aruba (Netherlands)', 'Active', 18),
//        ('Australia', 'Active', 18),
//        ('Austria', 'Active', 18),
//        ('Azerbaijan', 'Active', 18),
//        ('Bahamas', 'Active', 18),
//        ('Bahrain', 'Active', 18),
//        ('Bangladesh', 'Active', 18),
//        ('Barbados', 'Active', 18),
//        ('Belarus', 'Active', 18),
//        ('Belgium', 'Active', 18),
//        ('Belize', 'Active', 18),
//        ('Benin', 'Active', 18),
//        ('Bermuda (BOT)', 'Active', 18),
//        ('Bhutan', 'Active', 18),
//        ('Bolivia', 'Active', 18),
//        ('Bonaire (Netherlands)', 'Active', 18),
//        ('Bosnia and Herzegovina', 'Active', 18),
//        ('Botswana', 'Active', 18),
//        ('Brazil', 'Active', 18),
//        ('British Virgin Islands (BOT)', 'Active', 18),
//        ('Brunei', 'Active', 18),
//        ('Bulgaria', 'Active', 18),
//        ('Burkina Faso', 'Active', 18),
//        ('Burundi', 'Active', 18),
//        ('Cambodia', 'Active', 18),
//        ('Cameroon', 'Active', 18),
//        ('Canada', 'Active', 18),
//        ('Cape Verde', 'Active', 18),
//        ('Cayman Islands (BOT)', 'Active', 18),
//        ('Central African Republic', 'Active', 18),
//        ('Chad', 'Active', 18),
//        ('Chile', 'Active', 18),
//        ('China', 'Active', 18),
//        ('Christmas Island (Australia)', 'Active', 18),
//        ('Cocos (Keeling) Islands (Australia)', 'Active', 18),
//        ('Colombia', 'Active', 18),
//        ('Comoros', 'Active', 18),
//        ('Congo', 'Active', 18),
//        ('Cook Islands', 'Active', 18),
//        ('Costa Rica', 'Active', 18),
//        ('Croatia', 'Active', 18),
//        ('Cuba', 'Active', 18),
//        ('Curaçao (Netherlands)', 'Active', 18),
//        ('Cyprus', 'Active', 18),
//        ('Czech Republic', 'Active', 18),
//        ('Denmark', 'Active', 18),
//        ('Djibouti', 'Active', 18),
//        ('Dominica', 'Active', 18),
//        ('Dominican Republic', 'Active', 18),
//        ('DR Congo', 'Active', 18),
//        ('East Timor', 'Active', 18),
//        ('Ecuador', 'Active', 18),
//        ('Egypt', 'Active', 18),
//        ('El Salvador', 'Active', 18),
//        ('Equatorial Guinea', 'Active', 18),
//        ('Eritrea', 'Active', 18),
//        ('Estonia', 'Active', 18),
//        ('Eswatini', 'Active', 18),
//        ('Ethiopia', 'Active', 18),
//        ('Falkland Islands (BOT)', 'Active', 18),
//        ('Faroe Islands (Denmark)', 'Active', 18),
//        ('Fiji', 'Active', 18),
//        ('Finland', 'Active', 18),
//        ('France', 'Active', 18),
//        ('French Guiana (France)', 'Active', 18),
//        ('French Polynesia (France)', 'Active', 18),
//        ('Gabon', 'Active', 18),
//        ('Gambia', 'Active', 18),
//        ('Georgia', 'Active', 18),
//        ('Germany', 'Active', 18),
//        ('Ghana', 'Active', 18),
//        ('Gibraltar (BOT)', 'Active', 18),
//        ('Greece', 'Active', 18),
//        ('Greenland (Denmark)', 'Active', 18),
//        ('Grenada', 'Active', 18),
//        ('Guadeloupe (France)', 'Active', 18),
//        ('Guam (US)', 'Active', 18),
//        ('Guatemala', 'Active', 18),
//        ('Guernsey (Crown Dependency)', 'Active', 18),
//        ('Guinea', 'Active', 18),
//        ('Guinea-Bissau', 'Active', 18),
//        ('Guyana', 'Active', 18),
//        ('Haiti', 'Active', 18),
//        ('Honduras', 'Active', 18),
//        ('Hong Kong', 'Active', 18),
//        ('Hungary', 'Active', 18),
//        ('Iceland', 'Active', 18),
//        ('India', 'Active', 18),
//        ('Indonesia', 'Active', 18),
//        ('Iran', 'Active', 18),
//        ('Iraq', 'Active', 18),
//        ('Ireland', 'Active', 18),
//        ('Isle of Man (Crown Dependency)', 'Active', 18),
//        ('Israel', 'Active', 18),
//        ('Italy', 'Active', 18),
//        ('Ivory Coast', 'Active', 18),
//        ('Jamaica', 'Active', 18),
//        ('Japan', 'Active', 18),
//        ('Jersey (Crown Dependency)', 'Active', 18),
//        ('Jordan', 'Active', 18),
//        ('Kazakhstan', 'Active', 18),
//        ('Kenya', 'Active', 18),
//        ('Kiribati', 'Active', 18),
//        ('Kosovo', 'Active', 18),
//        ('Kuwait', 'Active', 18),
//        ('Kyrgyzstan', 'Active', 18),
//        ('Laos', 'Active', 18),
//        ('Latvia', 'Active', 18),
//        ('Lebanon', 'Active', 18),
//        ('Lesotho', 'Active', 18),
//        ('Liberia', 'Active', 18),
//        ('Libya', 'Active', 18),
//        ('Liechtenstein', 'Active', 18),
//        ('Lithuania', 'Active', 18),
//        ('Luxembourg', 'Active', 18),
//        ('Macau', 'Active', 18),
//        ('Madagascar', 'Active', 18),
//        ('Malawi', 'Active', 18),
//        ('Malaysia', 'Active', 18),
//        ('Maldives', 'Active', 18),
//        ('Mali', 'Active', 18),
//        ('Malta', 'Active', 18),
//        ('Marshall Islands', 'Active', 18),
//        ('Martinique (France)', 'Active', 18),
//        ('Mauritania', 'Active', 18),
//        ('Mauritius', 'Active', 18),
//        ('Mayotte (France)', 'Active', 18),
//        ('Mexico', 'Active', 18),
//        ('Micronesia', 'Active', 18),
//        ('Moldova', 'Active', 18),
//        ('Monaco', 'Active', 18),
//        ('Mongolia', 'Active', 18),
//        ('Montenegro', 'Active', 18),
//        ('Montserrat (BOT)', 'Active', 18),
//        ('Morocco', 'Active', 18),
//        ('Mozambique', 'Active', 18),
//        ('Myanmar', 'Active', 18),
//        ('Namibia', 'Active', 18),
//        ('Nauru', 'Active', 18),
//        ('Nepal', 'Active', 18),
//        ('Netherlands', 'Active', 18),
//        ('New Caledonia (France)', 'Active', 18),
//        ('New Zealand', 'Active', 18),
//        ('Nicaragua', 'Active', 18),
//        ('Niger', 'Active', 18),
//        ('Nigeria', 'Active', 18),
//        ('Niue', 'Active', 18),
//        ('Norfolk Island (Australia)', 'Active', 18),
//        ('North Korea', 'Active', 18),
//        ('North Macedonia', 'Active', 18),
//        ('Northern Cyprus', 'Active', 18),
//        ('Northern Mariana Islands (US)', 'Active', 18),
//        ('Norway', 'Active', 18),
//        ('Oman', 'Active', 18),
//        ('Pakistan', 'Active', 18),
//        ('Palau', 'Active', 18),
//        ('Palestine', 'Active', 18),
//        ('Panama', 'Active', 18),
//        ('Papua New Guinea', 'Active', 18),
//        ('Paraguay', 'Active', 18),
//        ('Peru', 'Active', 18),
//        ('Philippines', 'Active', 18),
//        ('Pitcairn Islands (BOT)', 'Active', 18),
//        ('Poland', 'Active', 18),
//        ('Portugal', 'Active', 18),
//        ('Puerto Rico (US)', 'Active', 18),
//        ('Qatar', 'Active', 18),
//        ('Réunion (France)', 'Active', 18),
//        ('Romania', 'Active', 18),
//        ('Russia', 'Active', 18),
//        ('Rwanda', 'Active', 18),
//        ('Saba (Netherlands)', 'Active', 18),
//        ('Saint Barthélemy (France)', 'Active', 18),
//        ('Saint Helena, Ascension and Tristan da Cunha (BOT)', 'Active', 18),
//        ('Saint Kitts and Nevis', 'Active', 18),
//        ('Saint Lucia', 'Active', 18),
//        ('Saint Martin (France)', 'Active', 18),
//        ('Saint Pierre and Miquelon (France)', 'Active', 18),
//        ('Saint Vincent and the Grenadines', 'Active', 18),
//        ('Samoa', 'Active', 18),
//        ('San Marino', 'Active', 18),
//        ('São Tomé and Príncipe', 'Active', 18),
//        ('Saudi Arabia', 'Active', 18),
//        ('Senegal', 'Active', 18),
//        ('Serbia', 'Active', 18),
//        ('Seychelles', 'Active', 18),
//        ('Sierra Leone', 'Active', 18),
//        ('Singapore', 'Active', 18),
//        ('Sint Eustatius (Netherlands)', 'Active', 18),
//        ('Sint Maarten (Netherlands)', 'Active', 18),
//        ('Slovakia', 'Active', 18),
//        ('Slovenia', 'Active', 18),
//        ('Solomon Islands', 'Active', 18),
//        ('Somalia', 'Active', 18),
//        ('South Africa', 'Active', 18),
//        ('South Korea', 'Active', 18),
//        ('South Sudan', 'Active', 18),
//        ('Spain', 'Active', 18),
//        ('Sri Lanka', 'Active', 18),
//        ('Sudan', 'Active', 18),
//        ('Suriname', 'Active', 18),
//        ('Svalbard and Jan Mayen (Norway)', 'Active', 18),
//        ('Sweden', 'Active', 18),
//        ('Switzerland', 'Active', 18),
//        ('Syria', 'Active', 18),
//        ('Taiwan', 'Active', 18),
//        ('Tajikistan', 'Active', 18),
//        ('Tanzania', 'Active', 18),
//        ('Thailand', 'Active', 18),
//        ('Togo', 'Active', 18),
//        ('Tokelau (NZ)', 'Active', 18),
//        ('Tonga', 'Active', 18),
//        ('Transnistria', 'Active', 18),
//        ('Trinidad and Tobago', 'Active', 18),
//        ('Tunisia', 'Active', 18),
//        ('Turkey', 'Active', 18),
//        ('Turkmenistan', 'Active', 18),
//        ('Turks and Caicos Islands (BOT)', 'Active', 18),
//        ('Tuvalu', 'Active', 18),
//        ('U.S. Virgin Islands (US)', 'Active', 18),
//        ('Uganda', 'Active', 18),
//        ('Ukraine', 'Active', 18),
//        ('United Arab Emirates', 'Active', 18),
//        ('United Kingdom', 'Active', 18),
//        ('United States', 'Active', 18),
//        ('Uruguay', 'Active', 18),
//        ('Uzbekistan', 'Active', 18),
//        ('Vanuatu', 'Active', 18),
//        ('Vatican City', 'Active', 18),
//        ('Venezuela', 'Active', 18),
//        ('Vietnam', 'Active', 18),
//        ('Wallis and Futuna (France)', 'Active', 18),
//        ('Western Sahara', 'Active', 18),
//        ('Yemen', 'Active', 18),
//        ('Zambia', 'Active', 18),
//        ('Zimbabwe', 'Active', 18);


//    INSERT INTO product_field_values (name, status, product_field_id)
//    VALUES
//        ('SALSEL International Documents', 'Active', 19),
//        ('SALSEL International Parcel', 'Active', 19),
//        ('SALSEL International Priority', 'Active', 19),
//        ('SALSEL International Express', 'Active', 19),
//        ('SALSEL International Promo Service', 'Active', 19),
//        ('SALSEL International Ground Shipping', 'Active', 19),
//        ('SALSEL International Inbound Document', 'Active', 19),
//        ('SALSEL International Inbound Parcel', 'Active', 19),
//        ('SALSEL International Inbound Others', 'Active', 19);