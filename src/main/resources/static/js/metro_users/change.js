$(document).ready(function() {
    $(function(){

        $("#change").click(function(){

          var check = false;
          $('#ajax_create_soldier *').filter(':input').each(function(){
              if($(this).val() == '' && $(this)[0].getAttribute('type') !== 'password' ){
                check = true;
              }
          });

        var login = $("#login").val();
        if (login == '') {
            $( "#login_validation" ).text("Введите логин");
            check = true;
        }

        var name = $("#name").val();
        if (name == '') {
            $( "#name_validation" ).text("Введите имя");
            check = true;
        }

        var surname = $("#surname").val();
        if (surname == '') {
            $( "#surname_validation" ).text("Введите фамилию");
            check = true;
        }

        var patronymic = $("#patronymic").val();
        if (patronymic == '') {
            $( "#patronymic_validation" ).text("Введите отчество");
            check = true;
        }

        var role = $("#role").val();
        console.log(role);
        if (role == '') {
            $( "#role_validation" ).text("Выберите роль пользователя");
            check = true;
        }else{
            $( "#role_validation" ).text('');
        }

          if (check == true){
            return;
          }

          var new_user = {
            "user_id":$("#user_id").val(),
            "role": $("#role").val(),
            "name": $("#name").val(),
            "surname": $("#surname").val(),
            "patronymic": $("#patronymic").val(),
            "login": $("#login").val(),
            "password": $("#password").val()
          };

          $.ajax({
            type: "POST",
            url: "/users/change",
            contentType: "application/json",
            data: JSON.stringify(new_user),
            // dataType: "dataType",
            success: function (data) {
              console.log("success");
              console.log(data);
              window.location.href = '/users';
            }
          });
          console.log(new_soldier);
        });
    })

    $( "#login" )
      .change(function() {

       event.preventDefault();

       var regExp = new RegExp("^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z]+(?<![_.])$");
       var OK = regExp.exec($("#login").val());
       if(!OK){
         $( "#login_validation" ).text("Не правильно введен логин. Логин должен иметь от 3 до 20 символов. Разрешены только анлийские буквы.");
         $("#change").prop("disabled",true);

       }else{
          $( "#login_validation" ).text("");
          $("#change").prop("disabled",false);
       }
    });

    $( "#name" )
      .change(function() {

       event.preventDefault();
       var regExp = new RegExp("^(?=.{2,20}$)(?![_.])(?!.*[_.]{2})[а-яА-ЯёЁ]+(?<![_.])$");
       var OK = regExp.exec($("#name").val());
       if(!OK){
         $( "#name_validation" ).text("Не правильно введено имя. Имя должно иметь от 2 до 100 символов и содержать только русские юуквы");
         $("#change").prop("disabled",true);

       }else{
          $( "#name_validation" ).text("");
          $("#change").prop("disabled",false);
       }
    });

    $( "#surname" )
      .change(function() {

       event.preventDefault();
       var regExp = new RegExp("^(?=.{2,20}$)(?![_.])(?!.*[_.]{2})[а-яА-ЯёЁ]+(?<![_.])$");
       var OK = regExp.exec($("#surname").val());
       if(!OK){
         $( "#surname_validation" ).text("Не правильно введена фамилия. Фамилия должна иметь от 2 до 100 символов и содержать только русские буквы");
         $("#change").prop("disabled",true);

       }else{
          $( "#surname_validation" ).text("");
          $("#change").prop("disabled",false);
       }
    });

    $( "#patronymic" )
      .change(function() {

       event.preventDefault();
       var regExp = new RegExp("^(?=.{2,20}$)(?![_.])(?!.*[_.]{2})[а-яА-ЯёЁ]+(?<![_.])$");
       var OK = regExp.exec($("#patronymic").val());
       if(!OK){
         $( "#patronymic_validation" ).text("Не правильно введено отчество. Отчество должно иметь от 2 до 100 символов и содержать только русские буквы");
         $("#change").prop("disabled",true);

       }else{
          $( "#patronymic_validation" ).text("");
          $("#change").prop("disabled",false);
       }
    });

    $( "#password" )
      .change(function() {

       event.preventDefault();
       var regExp = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
       console.log($("#password").val())
       if ($("#password").val() == '' || $("#password").val().length === 0){
        $( "#password_validation" ).text("");
       }

       var OK = regExp.exec($("#password").val());
       if(!OK){
         $( "#password_validation" ).text("Не правильно введен пароль. Пароль должен быть написан на английском языке," +
            " содержать минимум одну заглавную и прописную буквы, один спец символ, одну цифру и должен быть длинее 8 символов");
       }else{
          $( "#password_validation" ).text("");
       }
    });

});