# GoLand Find Internal Usages Agent

## Описание

Попытка самим исправить баг в [GoLand](https://www.jetbrains.com/go/), когда `Find Usages` не может найти использования
поле структуры объявленной в `internal` пакете, использованной через alias типа.

Баг в JetBrains YouTrack: https://youtrack.jetbrains.com/issue/GO-14075

## Рекомендация по использованию

Не стоит это использовать повседневно. Данный способ стоит использовать только во время массового рефакторинга /
исследования.

## Сборка

1. Клонируем проект
2. Выполняем `./gradlew build`
3. Результат сборки будет находиться в `./build/libs/goland-agent-${version}.jar`

## Установка

1. Запускаем `GoLand`
2. В главном меню выбрать `Help` > `Edit Custom VM Options`
3. В конце прописать `-javaagent:путь_до_goland-agent-${version}.jar`

## Настройка

### Включение debug режима

> Логи пишутся только в **файл**. Вывод в stdout / stderr в `idea.log` на данном этапе не доступна.

В данный момент есть два способа:

1. Устанавливаем env `GLA_DEBUG=log_path` и передаём путь до файла с логами
2. Устанавливаем параметр `-Dgla.debug=log_path` и передаём путь до файла с логами

Например:

```shell
GLA_DEBUG=/tmp/gla.log goland
# или
-Dgla.debug=/tmp/gla.log
```
