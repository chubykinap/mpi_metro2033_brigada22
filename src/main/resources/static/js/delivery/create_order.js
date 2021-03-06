$(document).ready(function() {
    $(function() {
        $("#create").click(function(event) {
            event.preventDefault();
            check = false;

            check = validate_items();
            check = validate_courier();
            check = validate_station();
            check = validate_date();

            if (check == true){
                alert("Не все данные заполнены!");
                return;
            }

            if (check_weight_sum() > 60){
                $( "#weight_validation" ).text("Превышение веса заказа");
                alert("Превышен допустимый вес заказа");
                return;
            } else {
                $( "#weight_validation" ).text("");
            }

            var order = {
                "courier_id":$("#courier").val(),
                "station":$("#station").val(),
                "direction":$("#direction").text(),
                "date":$('#date_select').val(),
                "items":getTableData()
            };

            $.ajax({
                type: "POST",
                url: "/delivery",
                contentType: "application/json",
                data: JSON.stringify(order),
                success: function (data) {
                    if (data.status == "Error"){
                        alert(data.data);
                        return;
                    }
                    console.log("success");
                    window.location.href = '/delivery';
                }
            });
        });

        $("#search").click(function(event) {
            event.preventDefault();
            var search = $("#search_text").val();

            $("#t_items tbody tr").each(function(i) {
                var $item_name = $(this).find('#t_name').text();
                if ($item_name != "" &&
                    $item_name.indexOf(search) < 0) {
                        $(this).hide();
                    } else {
                        $(this).show();
                    }
            });
        });

        $("#courier").on("change", function(){
            event.preventDefault();
            validate_courier();
        });

        $("#station").on("change", function(){
            event.preventDefault();
            validate_station();
        });

        $("#date_select").on("change", function(){
            event.preventDefault();
            validate_date();
        });
    })

    $(document).on('input','#t_quantity', function(event){
        event.preventDefault();
        calculate_weight($(this).closest("tr"));
        validate_items();
        if (check_weight_sum() > 60){
            $( "#weight_validation" ).text("Превышение веса заказа");
        } else {
            $( "#weight_validation" ).text("");
        }
    });
});

function getTableData() {
    var $t_data = [];
    var weight = 0;
    $("#t_items tbody tr").each(function(i) {
        var $c_data = [];
        var $item_name = $(this).find('#t_name').text();
        var $item_quantity = $(this).find('#t_quantity').val();
        if ($item_quantity > 0) {
            $c_data.push($item_name);
            $c_data.push($item_quantity);
            $t_data.push($c_data);
        }
    });
    return $t_data;
}

function check_weight_sum() {
    var weight = 0;
    $("#t_items tbody tr").each(function(i) {
        var val = parseInt($(this).find('#t_weight').text());
        if (val >= 0)
            weight += val;
    });
    return weight;
}

function validate_courier(){
    var courier = $("#courier").val();
    if (courier == '') {
        $( "#courier_validation" ).text("Выберите курьера");
        return true;
    }else{
        $( "#courier_validation" ).text("");
        return false;
    }
}

function validate_station(){
    var station = $("#station").val();
    if (station == '') {
        $( "#station_validation" ).text("Выберите станцию");
        return true;
    }else{
        $( "#station_validation" ).text("");
        return false;
    }
}

function validate_date(){
    var date = $("#date_select").val();
    if (date == '') {
        $( "#date_validation" ).text("Выберите дату доставки");
        return true;
    }else{
        $( "#date_validation" ).text("");
        return false;
    }
}

function validate_items(){
    var items = getTableData();
    if (items.length < 1) {
        $( "#item_validation" ).text("Не добавлены предметы для заказа");
        return true;
    }else{
        $( "#item_validation" ).text("");
        return false;
    }
}

function calculate_weight(tr){
    var weight = tr.find('#t_one_weight').text();
    var count = tr.find('#t_quantity').val();
    var $sum = tr.find('#t_weight');
    $sum.text(weight * count);
}
