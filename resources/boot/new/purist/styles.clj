(ns {{name}}.styles
    (:require
     [garden.def :as gd :refer [defstyles]]
     [garden.units :as u :refer [px percent s]]
     [garden.color :refer [rgba] :as color]))

(defstyles layout
  [:.stacked {:display 'flex}
   [:&.v {:flex-direction 'column}]
   [:&.h {:flex-direction 'row}]])

(defstyles typography
  [:body {:font-family 'Roboto
          :font-size (px 16)}])

(defstyles base
  typography
  layout
  [:body {:background-color 'wheat
          :margin 0}])

(defstyles combined
  base)

