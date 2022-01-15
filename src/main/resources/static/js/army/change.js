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

          var strength = $("#strength").val();
          if (strength == '') {
              $( "#strength_validation" ).text("Заполните параметр силы");
              check = true;
          }else{
               $( "#strength_validation" ).text("");
          }

          var stamina = $("#stamina").val();
          if (stamina == '') {
              $( "#stamina_validation" ).text("Заполните параметр выносливости");
              check = true;
          }else{
               $( "#stamina_validation" ).text("");
          }

          if (check == true){
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
              console.log(data);
              window.location.href = '/army';
            }
          });
          console.log(new_soldier);
        });
    })

    $("#agility").on('keyup', function(){

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
                 $("#change").prop("disabled",true);
               }else{
                  $( "#agility_validation" ).text("");
                  $("#change").prop("disabled",false);
               }
    })

    $("#strength").on('keyup', function(){

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
                 $("#change").prop("disabled",true);
               }else{
                  $( "#strength_validation" ).text("");
                  $("#change").prop("disabled",false);
               }
    })

    $("#stamina").on('keyup', function(){

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
                 $("#change").prop("disabled",true);
               }else{
                  $( "#stamina_validation" ).text("");
                  $("#change").prop("disabled",false);
               }
    })

});