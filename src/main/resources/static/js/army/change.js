$(document).ready(function() {
    $(function(){

        $("#change").click(function(){

          check = false;

          check_post = false;

            if ($('#post')[0].selectedIndex <= 0){
                check_post = true;
                $( "#post_validation" ).text("");
            }else{
                if ($('#post').val() == ''){
                    $( "#post_validation" ).text("Назначьте пост");
                    check = true;
                }else{
                    $( "#post_validation" ).text("");
                }
            }

          var rank = $("#rank").val();
          if (rank == '') {
              $( "#rank_validation" ).text("Выберите звание");
              check = true;
          }else{
              $( "#rank_validation" ).text("");
          }

          var health_state = $("#health_state").val();
          if (health_state == '') {
              $( "#health_validation" ).text("Выберите состояние здоровья");
              check = true;
          }else{
              $( "#health_validation" ).text("");
          }

          var agility = $("#agility").val();
          if (agility == '') {
              $( "#agility_validation" ).text("Заполните параметр ловкости");
              check = true;
          }else{
               $( "#agility_validation" ).text("");
          }

          if(validate_agility()){
            check = true;
          }

          var strength = $("#strength").val();
          if (strength == '') {
              $( "#strength_validation" ).text("Заполните параметр силы");
              check = true;
          }else{
               $( "#strength_validation" ).text("");
          }

          if(validate_strength()){
            check = true;
          }

          var stamina = $("#stamina").val();
          if (stamina == '') {
              $( "#stamina_validation" ).text("Заполните параметр выносливости");
              check = true;
          }else{
               $( "#stamina_validation" ).text("");
          }

          if(validate_stamina()){
            check = true;
          }

          if (check == true){
            alert("Введите данные правильно");
            return;
          }

          console.log($("#user").val());
          console.log($("#post").val());
          console.log($("#rank").val());
          console.log($("#health_state").val());
          console.log($("#agility").val());
          console.log($("#strength").val());
          console.log($("#stamina").val());

          post = -1;
          if (check_post == false){
            post = $("#post").val();
          }

          var new_soldier = {
            "soldier_id":$("#soldier_id").val(),
            "user_id":$("#user").val(),
            "post_id":post,
            "rank": $("#rank").val(),
            "health_state": $("#health_state").val(),
            "agility": $("#agility").val(),
            "strength": $("#strength").val(),
            "stamina": $("#stamina").val()
          };

          $.ajax({
            type: "POST",
            url: "/army/change",
            contentType: "application/json",
            data: JSON.stringify(new_soldier),
            // dataType: "dataType",
            success: function (data) {
              console.log("success");
              if (data.status == "Error"){
                alert(data.data);
                return;
              }
             else{
                show_notification(data.data);
              }
            },
            error: function (e){
                console.log(e);
            }
          });
          console.log(new_soldier);
        });
    })

     function show_notification(info){
        $('#notification').text("Данные солдата " + info.login + " " + info.name + " " + info.surname + " " + info.rank + " изменены");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }

    function validate_agility(){
        var value = $("#agility").val();

        var check = false;
        var regExp = /\d+/g;
        var OK = regExp.exec(value);
        console.log(value)
        console.log(OK)
        if (!OK){
        console.log("!OK")
        check = true;
        }

        if (value < 0 || value > 100){
        console.log("value < 0 ")
        check = true;
        }

        if(check){
         $( "#agility_validation" ).text("Не правильно задан параметр ловкость. Он должен быть целым числом от 0 до 100");
         return true;
        }else{
          $( "#agility_validation" ).text("");
          return false;
        }
    }

    $("#agility").on('keyup', function(){
        event.preventDefault();
        validate_agility();
    })

    function validate_strength(){
        var value = $("#strength").val();

        var check = false;
        var regExp = /\d+/g;
        var OK = regExp.exec(value);
        console.log(value)
        console.log(OK)
        if (!OK){
        console.log("!OK")
        check = true;
        }

        if (value < 0 || value > 100){
        console.log("value < 0 ")
        check = true;
        }

        if(check){
         $( "#strength_validation" ).text("Не правильно задан параметр сила. Он должен быть целым числом от 0 до 100");
         return true;
        }else{
          $( "#strength_validation" ).text("");
          return false;
        }
    }

    $("#strength").on('keyup', function(){
        event.preventDefault();
        validate_strength();
    })

    function validate_stamina(){
        var value = $("#stamina").val();

        var check = false;
        var regExp = /\d+/g;
        var OK = regExp.exec(value);
        console.log(value)
        console.log(OK)
        if (!OK){
        console.log("!OK")
        check = true;
        }

        if (value < 0 || value > 100){
        console.log("value < 0 ")
        check = true;
        }

        if(check){
         $( "#stamina_validation" ).text("Не правильно задан параметр выносливость. Он должен быть целым числом от 0 до 100");
         return true;
        }else{
          $( "#stamina_validation" ).text("");
          return false;
        }

    }

    $("#stamina").on('keyup', function(){
        event.preventDefault();
        validate_stamina();
    })

});