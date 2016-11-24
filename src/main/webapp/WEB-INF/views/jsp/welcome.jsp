<%@page session="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Spring MVC 4 + Ajax Hello World</title>

    <c:url var="home" value="/" scope="request"/>

    <spring:url value="/resources/core/css/hello.css" var="coreCss"/>
    <spring:url value="/resources/core/css/bootstrap.min.css"
                var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>
    <link href="${coreCss}" rel="stylesheet"/>

    <spring:url value="/resources/core/js/jquery.1.10.2.min.js"
                var="jqueryJs"/>
    <script src="${jqueryJs}"></script>
</head>

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Spring 4 MVC Ajax Hello World</a>
        </div>
    </div>
</nav>

<div class="container" style="min-height: 500px">

    <div class="starter-template">
        <h1>Search Form</h1>
        <br>

        <div id="feedback"></div>

        <form class="form-horizontal" id="search-form">
            <div class="form-group form-group-lg">
                <label class="col-sm-2 control-label">Username</label>

                <div class="col-sm-10">
                    <input type=text class="form-control" id="username">
                </div>
            </div>
            <div class="form-group form-group-lg">
                <label class="col-sm-2 control-label">Email</label>

                <div class="col-sm-10">
                    <input type="text" class="form-control" id="email">
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" id="bth-search"
                            class="btn btn-primary btn-lg">Search
                    </button>
                </div>
            </div>
        </form>

    </div>

</div>

<div class="container">
    <footer>
        <p>
            &copy; <a href="http://www.mkyong.com">Mkyong.com</a> 2015
        </p>
    </footer>
</div>

<script>
    var token = '';
    jQuery(document).ready(function ($) {
        getToken();
        $("#search-form").submit(function (event) {
            // Disble the search button
            enableSearchButton(false);
            // Prevent the form from submitting via the browser.
            event.preventDefault();
            searchViaAjax();
        });
    });

    function searchViaAjax() {
        var search = {}
        search["username"] = $("#username").val();
        search["email"] = $("#email").val();
        search['token']= token;

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "${home}search/api/getSearchResult",
            data: JSON.stringify(search),
            dataType: 'json',
            timeout: 100000,
            success: function (data) {
                console.log("SUCCESS: ", data);
                display(data);
            },
            error: function (e) {
                console.log("ERROR: ", e);
                display(e);
            },
            done: function (e) {
                console.log("DONE");
                enableSearchButton(true);
            }
        });

    }

    function getToken() {
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "${home}api/token/acquire",
            success: function (data) {
                token = data;
                alert(data);
                console.log("SUCCESS: ", data);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function enableSearchButton(flag) {
        $("#btn-search").prop("disabled", flag);
    }

    function display(data) {
        var json = "<h4>Ajax Response</h4><pre>"
                + JSON.stringify(data, null, 4) + "</pre>";
        $('#feedback').html(json);
    }
</script>

</body>
</html>