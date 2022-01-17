$(function(){

    $(document).ready(function() {
        $(function(){

          var table = document.getElementsByTagName('table')[0],
          rows = table.getElementsByTagName('tr'),
          text = 'textContent' in document ? 'textContent' : 'innerText';

          for (var i = 1, len = rows.length; i < len; i++) {
            rows[i].children[0][text] = i + rows[i].children[0][text];
          }
        })
    });

    $(document).on('click', '#delete', function(event)
    {
        event.preventDefault();
        if (window.confirm("Удалить датчик?")){
            var tr = $(this).closest('tr');
            var sensor_id = $(this).closest('a').attr("value");
            if (sensor_id == '') {
              alert("Датчик не найден!");
              return;
            }

            var sensor = {
                "sensor_id": sensor_id
            };

            $.ajax({
                type: "POST",
                url: "/sensors/delete",
                contentType: "application/json",
                data: JSON.stringify(sensor),
                success: function (data) {
                  console.log("success");
                  if (data.status == "Error"){
                    alert(data.data);
                    return;
                  }
                  else{
                    show_notification(data.data);
                    tr.remove();
                  }
                },
                error: function (e){
                    console.log(e);
                }
            });
        }
     });

     function show_notification(info){
        $('#notification').text("Датчик " + info.name + " " + info.location + " удален");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }

})