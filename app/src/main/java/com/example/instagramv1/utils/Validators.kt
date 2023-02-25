package com.example.instagramv1.utils

object Validators {

    fun validateFullName(fullName : String?) : ValidationErrorMessage? {
        if(fullName == null || fullName.isBlank()){
            return ValidationErrorMessage.NAME_NOT_GIVEN
        }
        if(!fullNameValidator(fullName)){
            return ValidationErrorMessage.NAME_NOT_VALID
        }
        return null
    }

    fun validateUserName(userName: String?) : ValidationErrorMessage?{
       if(userName == null || userName.isBlank()){
           return ValidationErrorMessage.USER_NAME_NOT_GIVEN
       }
        if(!userNameValidator(userName)){
            return ValidationErrorMessage.USER_NAME_DOES_NOT_MEET_REQUIREMENTS
        }
        return null
    }

    fun validateEmail(email : String?) : ValidationErrorMessage?{
        if(email == null || email.isBlank()){
            return ValidationErrorMessage.EMAIL_NOT_GIVEN
        }
        if(!emailValidator(email)){
            return ValidationErrorMessage.EMAIL_NOT_VALID
        }
        return null
    }

    fun validatePhone(phone : String?) : ValidationErrorMessage?{
        if(phone == null || phone.isBlank()){
            return ValidationErrorMessage.PHONE_NOT_GIVEN
        }
        if(!phoneNumberValidator(phone)){
            return ValidationErrorMessage.PHONE_NOT_VALID
        }
        return null
    }

    fun validatePassword(password : String?) : ValidationErrorMessage?{
        if(password == null || password.isBlank()){
            return ValidationErrorMessage.PASSWORD_NOT_GIVEN
        }
        if(!passwordValidator(password)){
            return ValidationErrorMessage.PASSWORD_DOES_NOT_MEET_REQUIREMENTS
        }
        return null
    }

    fun validateConfirmPassword(password : String?, confirmPassword : String?) : ValidationErrorMessage?{
        if(confirmPassword == null|| confirmPassword.isBlank()){
            return ValidationErrorMessage.CONFIRM_PASSWORD_NOT_GIVEN
        }
        if(confirmPassword != password){
            return ValidationErrorMessage.PASSWORD_NOT_MATCHED
        }
        return null
    }



    private fun fullNameValidator(fullName: String): Boolean {
        return regexValidator("^[\\p{L}.'-][\\p{L} .'-]{1,23}[\\p{L}.']\$", fullName)

    }
    private fun userNameValidator(userName: String): Boolean {
        return regexValidator("^(?=.*[a-zA-Z].*[a-zA-Z].*[a-zA-Z])[a-zA-Z0-9_.]{4,20}\$",userName)

    }

    private fun emailValidator(email: String): Boolean {
        return regexValidator("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$",email)
    }

    private fun phoneNumberValidator(phoneNumber: String): Boolean {
        return regexValidator("([1-9]\\d{9})", phoneNumber)
    }

    private fun passwordValidator(password: String): Boolean{
        return regexValidator("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",password)
    }

    private fun regexValidator(regex : String, value : String): Boolean{
        val pattern = Regex(regex)
        return pattern.matches(value)
    }
}