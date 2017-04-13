<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"
       uri="http://java.sun.com/jsp/jstl/core" %>
<html ng-app="sampleApp" data-wf-domain="submit-form.webflow.io" data-wf-page="57d54219c8cbc2347a41b8f0" data-wf-site="57d54219c8cbc2347a41b8ef" data-wf-status="1" class=" w-mod-js w-mod-no-touch w-mod-video w-mod-no-ios">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset='utf-8'>
    <title>CeļoKopā.lv </title>
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <meta content="Webflow" name="generator">

    <meta property="og:url"           content="${url}" />
    <meta property="og:type"          content="${type}" />
    <meta property="og:title"         content="${title}" />
    <meta property="og:description"   content="${description}" />
    <meta property="og:image"         content="${image}" />
    <meta property="og:updated_time"  content="${updated}" />

    <link href="/resources/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css">
    <link href="/resources/css/bootstrap-theme.css" rel="stylesheet" type="text/css">
    <link href="/resources/css/fileinput.css" media="all" rel="stylesheet" type="text/css" />
    <link href="/resources/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/resources/bower_components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css">
    <link href="/resources/bower_components/tablesaw/dist/stackonly/tablesaw.stackonly.scss" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="https://twitter.github.io/typeahead.js/css/examples.css">
    <link href="/resources/css/style.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="/resources/bower_components/ng-dialog/css/ngDialog.css">
    <link rel="stylesheet" type="text/css" href="/resources/bower_components/ng-dialog/css/ngDialog-theme-default.css">
    <link rel="stylesheet" type="text/css" href="/resources/bower_components/bootstrap-language/languages.css">
    <link rel="stylesheet" type="text/css" href="/resources/bower_components/components-font-awesome/css/font-awesome.css">

    <script src="/resources/bower_components/angular/angular.min.js"></script>
    <script src="/resources/bower_components/angular-animate/angular-animate.min.js"></script>
    <script src="/resources/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
    <script src="/resources/bower_components/angular-cookies/angular-cookies.js"></script>
    <script src="/resources/bower_components/spring-security-csrf-token-interceptor/dist/spring-security-csrf-token-interceptor.min.js"></script>
    <script src="/resources/bower_components/angular-route/angular-route.min.js"></script>
    <script src="/resources/bower_components/angular-messages/angular-messages.min.js"></script>
    <script src="/resources/bower_components/jquery/dist/jquery.min.js"></script>
    <script src="/resources/bower_components/moment/moment.js"></script>
    <script src="/resources/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/resources/bower_components/bootstrap-validator/dist/validator.js"></script>
    <script src="/resources/bower_components/ng-dialog/js/ngDialog.js"></script>
    <script src="/resources/bower_components/bootstrap-fileinput/js/fileinput.js"></script>
    <script src="/resources/bower_components/tablesaw/dist/tablesaw.js"></script>
    <script src="/resources/bower_components/angular-translate/angular-translate.js"></script>
    <script src="/resources/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.js"></script>
    <script src="/resources/bower_components/typeahead.js/dist/bloodhound.js"></script>
    <script src="/resources/bower_components/typeahead.js/dist/typeahead.bundle.js"></script>
    <script src="/resources/bower_components/typeahead.js/dist/typeahead.jquery.js"></script>
    <script src="/resources/public/js/editable-table-widgets.js"></script>
    <script src="/resources/js/frontend-services.js"></script>
    <script src="/resources/public/js/services.js"></script>
    <script src="/resources/public/js/common.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>

    <script src="/resources/public/js/app.js"></script>

    <script src="/resources/bower_components/bootstrap/dist/js/bootstrap.js"></script>
    <base href="/">
</head>
<body ng-view>
<div>
    <header class="pure-g" ng-include="'/resources/partials/header.html'"></header>
    <div class="w-container">
        <tt-error-messages></tt-error-messages>
        <tt-success-messages></tt-success-messages>
    </div>
    <div class="search-form-container3">
        <div class="how-it-works-container w-container">
            <h1>{{ 'home.travel-cheaper' | translate }}</h1>
            <div>Выбирай маршрут и отправляйся навстречу приключениям.</div><a class="how-it-works-button w-button" href="#">Как это работает?</a>
        </div>
        <div class="search-form-continer2">
            <div class="search-form-container1 w-container">
                <div class="find-buddies">Найдите своих попутчиков и составьте им компанию.</div>
                <div class="w-form">
                    <form class="search-form" ng-submit="onSearch()">
                        <div class="w-row">
                            <div class="search-column w-col w-col-5">
                                <div class="search-div">
                                    <label class="search-field-label">Откуда?</label>
                                    <input id="from" placeholder="Пример: Rīga" ng-model="from" class="search-field w-input" />
                                </div>
                            </div>
                            <div class="search-column w-col w-col-5">
                                <div class="search-div">
                                    <label class="search-field-label">Куда?</label>
                                    <input id="to" placeholder="Пример: Daugavpils" ng-model="to" class="search-field w-input" />
                                </div>
                            </div>
                            <div class="last-column w-col w-col-2">
                                <input class="search-button w-button btn btn-primary" type="submit" value="Найти" />
                            </div>
                        </div>
                    </form>
                    <div class="w-form-done">
                        <div>Thank you! Your submission has been received!</div>
                    </div>
                    <div class="w-form-fail">
                        <div>Oops! Something went wrong while submitting the form</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="shortcut-section">
        <div class="w-container">
            <div class="travel-between-cities w-row">
                <div class="w-col w-col-6">
                    <h3 class="travel-together">Путешествуйте вместе</h3>
                    <div>Быстрее из пункта А в пункт Б.</div>
                </div>
                <div class="w-col w-col-2">
                    <div class="roue-shortcut">
                        <div class="towns-shortcut">Рига
                            <br>Даугавпилс</div>
                    </div>
                </div>
                <div class="w-col w-col-2">
                    <div class="roue-shortcut">
                        <div class="towns-shortcut">Юрмала
                            <br>Вентспилс</div>
                    </div>
                </div>
                <div class="w-col w-col-2">
                    <div class="roue-shortcut">
                        <div class="towns-shortcut">Елгава
                            <br>Рига</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="advantages-section">
        <div class="advantages-header-container w-container">
            <h1 class="advantages">Преимущества</h1>
        </div>
        <div class="w-container">
            <div class="w-row">
                <div class="advantages-column w-col w-col-6">
                    <div class="advantages-details">
                        <h2>Lorem Ipsum&nbsp;is simply</h2>
                        <div class="advantage-details">It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.</div><a class="read-more" href="#">Узнать больше</a>
                    </div>
                    <div class="advantages-picture-holder"><img height="100%" src="/resources/img/image_5f925b0a-faf3-46f9-a7cc-f5a31f8f3356.png" width="100%">
                    </div>
                    <div class="advantages-details">
                        <h2>There are many variations</h2>
                        <div class="advantage-details">All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet.</div><a class="read-more" href="#">Узнать больше</a>
                    </div>
                </div>
                <div class="advantages-column w-col w-col-6">
                    <div class="advantages-picture-holder"><img height="100%" src="/resources/img/pinguins.jpg" width="100%">
                    </div>
                    <div class="advantages-details">
                        <h2>Latin professor</h2>
                        <div class="advantage-details">Finibus Bonorum et Malorum" (The Extremes of Good and Evil) by Cicero, written in 45 BC.</div><a class="read-more" href="#">Узнать больше</a>
                    </div>
                    <div class="advantages-picture-holder"><img height="100%" src="/resources/img/Jellyfish.jpg" width="100%">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div ng-include="'/resources/partials/footer.html'"></div>
</div>
</body>
</html>