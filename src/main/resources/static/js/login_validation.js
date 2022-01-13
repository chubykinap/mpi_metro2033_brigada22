$(document).ready(function() {

    $( "#username" )
      .change(function() {

       event.preventDefault();

       var regExp = new RegExp("^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
       var OK = regExp.exec($("#username").val());
       if(!OK){
         $( "#username_validation_warning" ).text("Не правильно введен логин. Логин должен иметь от 3 до 20 символов. Разрешены только анлийские буквы.");
         $("#button").prop("disabled",true);

       }else{
          $( "#username_validation_warning" ).text("");
          $("#button").prop("disabled",false);
       }
    });
});

