(ns {{name}}.core
    (:require
     {{#om-now}}
     [om.core :as om]
     {{/om-now}}
     {{#om-next}}
     [om.next :as om :refer-macros [defui]]
     {{/om-next}}
     [om.dom :as dom]
     [goog.dom :as gdom]))

(enable-console-print!)

(def app-state (atom {}))

{{#om-now}}
(defn {{name}} [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
               "om loaded"))))

(om/root {{name}}
         app-state
         {:target (gdom/getElement "app")})
{{/om-now}}

{{#om-next}}
(defui Root
  Object
  (render [this]
          (dom/div nil "om loaded")))

(def reconciler
  (om/reconciler {:state app-state}))

(om/add-root! reconciler
              Root
              (gdom/getElement "app"))
{{/om-next}}

(defn main []
  (println "reloading"))
