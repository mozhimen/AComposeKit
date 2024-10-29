package com.mozhimen.composek.ui.autofill

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.autofill.AutofillType.AddressAuxiliaryDetails
import androidx.compose.ui.autofill.AutofillType.AddressCountry
import androidx.compose.ui.autofill.AutofillType.AddressLocality
import androidx.compose.ui.autofill.AutofillType.AddressRegion
import androidx.compose.ui.autofill.AutofillType.AddressStreet
import androidx.compose.ui.autofill.AutofillType.BirthDateDay
import androidx.compose.ui.autofill.AutofillType.BirthDateFull
import androidx.compose.ui.autofill.AutofillType.BirthDateMonth
import androidx.compose.ui.autofill.AutofillType.BirthDateYear
import androidx.compose.ui.autofill.AutofillType.CreditCardExpirationDate
import androidx.compose.ui.autofill.AutofillType.CreditCardExpirationDay
import androidx.compose.ui.autofill.AutofillType.CreditCardExpirationMonth
import androidx.compose.ui.autofill.AutofillType.CreditCardExpirationYear
import androidx.compose.ui.autofill.AutofillType.CreditCardNumber
import androidx.compose.ui.autofill.AutofillType.CreditCardSecurityCode
import androidx.compose.ui.autofill.AutofillType.EmailAddress
import androidx.compose.ui.autofill.AutofillType.Gender
import androidx.compose.ui.autofill.AutofillType.NewPassword
import androidx.compose.ui.autofill.AutofillType.NewUsername
import androidx.compose.ui.autofill.AutofillType.Password
import androidx.compose.ui.autofill.AutofillType.PersonFirstName
import androidx.compose.ui.autofill.AutofillType.PersonFullName
import androidx.compose.ui.autofill.AutofillType.PersonLastName
import androidx.compose.ui.autofill.AutofillType.PersonMiddleInitial
import androidx.compose.ui.autofill.AutofillType.PersonMiddleName
import androidx.compose.ui.autofill.AutofillType.PersonNamePrefix
import androidx.compose.ui.autofill.AutofillType.PersonNameSuffix
import androidx.compose.ui.autofill.AutofillType.PhoneCountryCode
import androidx.compose.ui.autofill.AutofillType.PhoneNumber
import androidx.compose.ui.autofill.AutofillType.PhoneNumberDevice
import androidx.compose.ui.autofill.AutofillType.PhoneNumberNational
import androidx.compose.ui.autofill.AutofillType.PostalAddress
import androidx.compose.ui.autofill.AutofillType.PostalCode
import androidx.compose.ui.autofill.AutofillType.PostalCodeExtended
import androidx.compose.ui.autofill.AutofillType.SmsOtpCode
import androidx.compose.ui.autofill.AutofillType.Username

/**
 * @ClassName AndroidAutofillType
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
/**
 * Gets the Android specific [AutofillHint][android.view.ViewStructure.setAutofillHints]
 * corresponding to the current [AutofillType].
 */
@ExperimentalComposeUiApi
internal val AutofillType.androidType: String
    get() {
        val androidAutofillType = androidAutofillTypes[this]
        requireNotNull(androidAutofillType, { "Unsupported autofill type" })
        return androidAutofillType
    }


/**
 * Maps each [AutofillType] to one of the  autofill hints in [androidx.autofill.HintConstants]
 */
@ExperimentalComposeUiApi
private val androidAutofillTypes: HashMap<AutofillType, String> = hashMapOf(
    EmailAddress to "emailAddress",
    Username to "username",
    Password to "password",
    NewUsername to "newUsername",
    NewPassword to "newPassword",
    PostalAddress to "postalAddress",
    PostalCode to "postalCode",
    CreditCardNumber to "creditCardNumber",
    CreditCardSecurityCode to "creditCardSecurityCode",
    CreditCardExpirationDate to "creditCardExpirationDate",
    CreditCardExpirationMonth to "creditCardExpirationMonth",
    CreditCardExpirationYear to "creditCardExpirationYear",
    CreditCardExpirationDay to "creditCardExpirationDay",
    AddressCountry to "addressCountry",
    AddressRegion to "addressRegion",
    AddressLocality to "addressLocality",
    AddressStreet to "streetAddress",
    AddressAuxiliaryDetails to "extendedAddress",
    PostalCodeExtended to "extendedPostalCode",
    PersonFullName to "personName",
    PersonFirstName to "personGivenName",
    PersonLastName to "personFamilyName",
    PersonMiddleName to "personMiddleName",
    PersonMiddleInitial to "personMiddleInitial",
    PersonNamePrefix to "personNamePrefix",
    PersonNameSuffix to "personNameSuffix",
    PhoneNumber to "phoneNumber",
    PhoneNumberDevice to "phoneNumberDevice",
    PhoneCountryCode to "phoneCountryCode",
    PhoneNumberNational to "phoneNational",
    Gender to "gender",
    BirthDateFull to "birthDateFull",
    BirthDateDay to "birthDateDay",
    BirthDateMonth to "birthDateMonth",
    BirthDateYear to "birthDateYear",
    SmsOtpCode to "smsOTPCode"
)
