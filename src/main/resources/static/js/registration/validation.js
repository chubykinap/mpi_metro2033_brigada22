$(document).ready(function() {

    $("#register_form").submit(function(e) {
      e.preventDefault();
      var check = false;

      var login = $("#login").val();
      if (login == '') {
          $( "#login_validation" ).text("Введите логин");
          check = true;
      }

      if(validate_login()){
        check = true;
      }

      var name = $("#name").val();
      if (name == '') {
          $( "#name_validation" ).text("Введите имя");
          check = true;
      }

      if(validate_name()){
        check = true;
      }

      var surname = $("#surname").val();
      if (surname == '') {
          $( "#surname_validation" ).text("Введите фамилию");
          check = true;
      }

      if(validate_surname()){
        check = true;
      }

      var patronymic = $("#patronymic").val();
      if (patronymic == '') {
          $( "#patronymic_validation" ).text("Введите отчество");
          check = true;
      }

      if(validate_patronymic()){
        check = true;
      }

      var password = $("#password").val();
      if (password == '') {
          $( "#password_validation" ).text("Введите пароль");
          check = true;
      }

      if(validate_password()){
        check = true;
      }

      var role = $("#role").val();
      console.log(role);
      if (role == '') {
          $( "#role_validation" ).text("Выбирете роль пользователя");
          check = true;
      }else{
          $( "#role_validation" ).text('');
      }

      if (check == false){
          $(this).unbind('submit').submit()
      }else{
        alert("Введите правильно данные");
      }
    });

    function validate_login(){
       var regExp = new RegExp("^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z]+(?<![_.])$");
       var OK = regExp.exec($("#login").val());
       if(!OK){
         $( "#login_validation" ).text("Не правильно введен логин. Логин должен иметь от 3 до 20 символов. Разрешены только анлийские буквы.");
         return true;
       }else{
          $( "#login_validation" ).text("");
          return false;
       }
    }

    function validate_name(){
       var regExp = new RegExp("^(?=.{2,20}$)(?![_.])(?!.*[_.]{2})[а-яА-ЯёЁ]+(?<![_.])$");
       var OK = regExp.exec($("#name").val());
       if(!OK){
         $( "#name_validation" ).text("Не правильно введено имя. Имя должно иметь от 2 до 100 символов и содержать только русские юуквы");
         return true;
       }else{
          $( "#name_validation" ).text("");
          return false;
       }
    }

    function validate_surname(){
       var regExp = new RegExp("^(?=.{2,20}$)(?![_.])(?!.*[_.]{2})[а-яА-ЯёЁ]+(?<![_.])$");
       var OK = regExp.exec($("#surname").val());
       if(!OK){
         $( "#surname_validation" ).text("Не правильно введена фамилия. Фамилия должна иметь от 2 до 100 символов и содержать только русские буквы");
         return true;
       }else{
          $( "#surname_validation" ).text("");
          return false;
       }
    }

    function validate_patronymic(){
       var regExp = new RegExp("^(?=.{2,20}$)(?![_.])(?!.*[_.]{2})[а-яА-ЯёЁ]+(?<![_.])$");
       var OK = regExp.exec($("#patronymic").val());
       if(!OK){
         $( "#patronymic_validation" ).text("Не правильно введено отчество. Отчество должно иметь от 2 до 100 символов и содержать только русские буквы");
         return true;
       }else{
          $( "#patronymic_validation" ).text("");
          return false;
       }
    }

    function validate_password(){
       var regExp = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
       console.log($("#password").val())
       var OK = regExp.exec($("#password").val());
       if(!OK){
         $( "#password_validation" ).text("Не правильно введен пароль. Пароль должен быть написан на английском языке," +
            " содержать минимум одну заглавную и прописную буквы, один спец символ, одну цифру и должен быть длинее 8 символов");
            return true;
       }else{
          $( "#password_validation" ).text("");
          return false;
       }
    }

    $("#login").on('keyup', function(){
       event.preventDefault();
       validate_login();
    });

    $("#name").on('keyup', function(){
       event.preventDefault();
       validate_name();
    });

    $("#surname").on('keyup', function(){
       event.preventDefault();
       validate_surname();
    });

    $("#patronymic").on('keyup', function(){
       event.preventDefault();
       validate_patronymic();
    });

    $("#password").on('keyup', function(){
       event.preventDefault();
       validate_password();
    });

});

