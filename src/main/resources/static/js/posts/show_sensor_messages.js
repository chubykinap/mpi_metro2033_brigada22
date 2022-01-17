$(function(){
    format_data();

    $(document).ready(function() {
        $(function(){

          var table = document.getElementsByTagName('table')[0],
          rows = table.getElementsByTagName('tr'),
          text = 'textContent' in document ? 'textContent' : 'innerText';

          for (var i = 1, len = rows.length; i < len; i++) {
            rows[i].children[0][text] = i + rows[i].children[0][text];
          }

            var table = document.getElementsByTagName('table')[1],
            rows = table.getElementsByTagName('tr'),
            text = 'textContent' in document ? 'textContent' : 'innerText';

            for (var i = 1, len = rows.length; i < len; i++) {
              rows[i].children[0][text] = i + rows[i].children[0][text];
            }

        })
    });

    $(document).on('click', '#problem_button', function(event)
    {
        event.preventDefault();
        if (window.confirm("Вы уверены?")){
              var sensor_id = $("#sensor_id").attr("value");
              if (sensor_id == '') {
                  alert("Произошла ошибка с id датчика");
                  check = true;
              }

            var sensor = {
                "sensor_id": sensor_id
            };

            $.ajax({
                type: "POST",
                url: "/sensors/done",
                contentType: "application/json",
                data: JSON.stringify(sensor),
                success: function (data) {
                  console.log("success");
                  if (data.status == "Error"){
                    alert(data.data);
                    return;
                  }
                  else{
                    $("#problem_button").hide();
                    clear_problem();
                  }
                },
                error: function (e){
                    console.log(e);
                }
            });
        }
     });

     function clear_problem(){
        console.log("Hererewrew");
        $('#table_sensor_id').css("background-color","white");
        $('#table_name').css("background-color","white");
        $('#table_location').css("background-color","white");
        $('#table_post').css("background-color","white");
        $('#table_status').css("background-color","white");

//        $('#no_post').css("background-color","white");

     }

     function show_notification(info){
        $('#notification').text("Датчик " + info.name + " " + info.location + " удален");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }

     function format_data(){
        $('.message_date_table').each(function(i, obj) {
            var table_date = obj.textContent;
            table_date = table_date.substring(0, table_date.indexOf('.'));
            table_date = table_date.replace('T', "  ");
            obj.textContent = table_date;
        });

        //s = s.substring(0, s.indexOf('?'));
     }
})