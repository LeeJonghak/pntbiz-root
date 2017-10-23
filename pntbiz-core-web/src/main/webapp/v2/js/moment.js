//! moment.js
//! version : 2.9.0
//! authors : Tim Wood, Iskren Chernev, Moment.js contributors
//! license : MIT
//! momentjs.com

(function (undefined) {
    /************************************
        Constants
    ************************************/

    var moment,
        VERSION = '2.9.0',
        // the global-scope this is NOT the global object in Node.js
        globalScope = (typeof global !== 'undefined' && (typeof window === 'undefined' || window === global.window)) ? global : this,
        oldGlobalMoment,
        round = Math.round,
        hasOwnProperty = Object.prototype.hasOwnProperty,
        i,

        YEAR = 0,
        MONTH = 1,
        DATE = 2,
        HOUR = 3,
        MINUTE = 4,
        SECOND = 5,
        MILLISECOND = 6,

        // internal storage for locale config files
        locales = {},

        // extra moment internal properties (plugins register props here)
        momentProperties = [],

        // check for nodeJS
        hasModule = (typeof module !== 'undefined' && module && module.exports),

        // ASP.NET json date format regex
        aspNetJsonRegex = /^\/?Date\((\-?\d+)/i,
        aspNetTimeSpanJsonRegex = /(\-)?(?:(\d*)\.)?(\d+)\:(\d+)(?:\:(\d+)\.?(\d{3})?)?/,

        // from http://docs.closure-library.googlecode.com/git/closure_goog_date_date.js.source.html
        // somewhat more in line with 4.4.3.2 2004 spec, but allows decimal anywhere
        isoDurationRegex = /^(-)?P(?:(?:([0-9,.]*)Y)?(?:([0-9,.]*)M)?(?:([0-9,.]*)D)?(?:T(?:([0-9,.]*)H)?(?:([0-9,.]*)M)?(?:([0-9,.]*)S)?)?|([0-9,.]*)W)$/,

        // format tokens
        formattingTokens = /(\[[^\[]*\])|(\\)?(Mo|MM?M?M?|Do|DDDo|DD?D?D?|ddd?d?|do?|w[o|w]?|W[o|W]?|Q|YYYYYY|YYYYY|YYYY|YY|gg(ggg?)?|GG(GGG?)?|e|E|a|A|hh?|HH?|mm?|ss?|S{1,4}|x|X|zz?|ZZ?|.)/g,
        localFormattingTokens = /(\[[^\[]*\])|(\\)?(LTS|LT|LL?L?L?|l{1,4})/g,

        // parsing token regexes
        parseTokenOneOrTwoDigits = /\d\d?/, // 0 - 99
        parseTokenOneToThreeDigits = /\d{1,3}/, // 0 - 999
        parseTokenOneToFourDigits = /\d{1,4}/, // 0 - 9999
        parseTokenOneToSixDigits = /[+\-]?\d{1,6}/, // -999,999 - 999,999
        parseTokenDigits = /\d+/, // nonzero number of digits
        parseTokenWord = /[0-9]*['a-z\u00A0-\u05FF\u0700-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+|[\u0600-\u06FF\/]+(\s*?[\u0600-\u06FF]+){1,2}/i, // any word (or two) characters or numbers including two/three word month in arabic.
        parseTokenTimezone = /Z|[\+\-]\d\d:?\d\d/gi, // +00:00 -00:00 +0000 -0000 or Z
        parseTokenT = /T/i, // T (ISO separator)
        parseTokenOffsetMs = /[\+\-]?\d+/, // 1234567890123
        parseTokenTimestampMs = /[\+\-]?\d+(\.\d{1,3})?/, // 123456789 123456789.123

        //strict parsing regexes
        parseTokenOneDigit = /\d/, // 0 - 9
        parseTokenTwoDigits = /\d\d/, // 00 - 99
        parseTokenThreeDigits = /\d{3}/, // 000 - 999
        parseTokenFourDigits = /\d{4}/, // 0000 - 9999
        parseTokenSixDigits = /[+-]?\d{6}/, // -999,999 - 999,999
        parseTokenSignedNumber = /[+-]?\d+/, // -inf - inf

        // iso 8601 regex
        // 0000-00-00 0000-W00 or 0000-W00-0 + T + 00 or 00:00 or 00:00:00 or 00:00:00.000 + +00:00 or +0000 or +00)
        isoRegex = /^\s*(?:[+-]\d{6}|\d{4})-(?:(\d\d-\d\d)|(W\d\d$)|(W\d\d-\d)|(\d\d\d))((T| )(\d\d(:\d\d(:\d\d(\.\d+)?)?)?)?([\+\-]\d\d(?::?\d\d)?|\s*Z)?)?$/,

        isoFormat = 'YYYY-MM-DDTHH:mm:ssZ',

        isoDates = [
            ['YYYYYY-MM-DD', /[+-]\d{6}-\d{2}-\d{2}/],
            ['YYYY-MM-DD', /\d{4}-\d{2}-\d{2}/],
            ['GGGG-[W]WW-E', /\d{4}-W\d{2}-\d/],
            ['GGGG-[W]WW', /\d{4}-W\d{2}/],
            ['YYYY-DDD', /\d{4}-\d{3}/]
        ],

        // iso time formats and regexes
        isoTimes = [
            ['HH:mm:ss.SSSS', /(T| )\d\d:\d\d:\d\d\.\d+/],
            ['HH:mm:ss', /(T| )\d\d:\d\d:\d\d/],
            ['HH:mm', /(T| )\d\d:\d\d/],
            ['HH', /(T| )\d\d/]
        ],

        // timezone chunker '+10:00' > ['10', '00'] or '-1530' > ['-', '15', '30']
        parseTimezoneChunker = /([\+\-]|\d\d)/gi,

        // getter and setter names
        proxyGettersAndSetters = 'Date|Hours|Minutes|Seconds|Milliseconds'.split('|'),
        unitMillisecondFactors = {
            'Milliseconds' : 1,
            'Seconds' : 1e3,
            'Minutes' : 6e4,
            'Hours' : 36e5,
            'Days' : 864e5,
            'Months' : 2592e6,
            'Years' : 31536e6
        },

        unitAliases = {
            ms : 'millisecond',
            s : 'second',
            m : 'minute',
            h : 'hour',
            d : 'day',
            D : 'date',
            w : 'week',
            W : 'isoWeek',
            M : 'month',
            Q : 'quarter',
            y : 'year',
            DDD : 'dayOfYear',
            e : 'weekday',
            E : 'isoWeekday',
            gg: 'weekYear',
            GG: 'isoWeekYear'
        },

        camelFunctions = {
            dayofyear : 'dayOfYear',
            isoweekday : 'isoWeekday',
            isoweek : 'isoWeek',
            weekyear : 'weekYear',
            isoweekyear : 'isoWeekYear'
        },

        // format function strings
        formatFunctions = {},

        // default relative time thresholds
        relativeTimeThresholds = {
            s: 45,  // seconds to minute
            m: 45,  // minutes to hour
            h: 22,  // hours to day
            d: 26,  // days to month
            M: 11   // months to year
        },

        // tokens to ordinalize and pad
        ordinalizeTokens = 'DDD w W M D d'.split(' '),
        paddedTokens = 'M D H h m s w W'.split(' '),

        formatTokenFunctions = {
            M    : function () {
                return this.month() + 1;
            },
            MMM  : function (format) {
                return this.localeData().monthsShort(this, format);
            },
            MMMM : function (format) {
                return this.localeData().months(this, format);
            },
            D    : function () {
                return this.date();
            },
            DDD  : function () {
                return this.dayOfYear();
            },
            d    : function () {
                return this.day();
            },
            dd   : function (format) {
                return this.localeData().weekdaysMin(this, format);
            },
            ddd  : function (format) {
                return this.localeData().weekdaysShort(this, format);
            },
            dddd : function (format) {
                return this.localeData().weekdays(this, format);
            },
            w    : function () {
                return this.week();
            },
            W    : function () {
                return this.isoWeek();
            },
            YY   : function () {
                return leftZeroFill(this.year() % 100, 2);
            },
            YYYY : function () {
                return leftZeroFill(this.year(), 4);
            },
            YYYYY : function () {
                return leftZeroFill(this.year(), 5);
            },
            YYYYYY : function () {
                var y = this.year(), sign = y >= 0 ? '+' : '-';
                return sign + leftZeroFill(Math.abs(y), 6);
            },
            gg   : function () {
                return leftZeroFill(this.weekYear() % 100, 2);
            },
            gggg : function () {
                return leftZeroFill(this.weekYear(), 4);
            },
            ggggg : function () {
                return leftZeroFill(this.weekYear(), 5);
            },
            GG   : function () {
                return leftZeroFill(this.isoWeekYear() % 100, 2);
            },
            GGGG : function () {
                return leftZeroFill(this.isoWeekYear(), 4);
            },
            GGGGG : function () {
                return leftZeroFill(this.isoWeekYear(), 5);
            },
            e : function () {
                return this.weekday();
            },
            E : function () {
                return this.isoWeekday();
            },
            a    : function () {
                return this.localeData().meridiem(this.hours(), this.minutes(), true);
            },
            A    : function () {
                return this.localeData().meridiem(this.hours(), this.minutes(), false);
            },
            H    : function () {
                return this.hours();
            },
            h    : function () {
                return this.hours() % 12 || 12;
            },
            m    : function () {
                return this.minutes();
            },
            s    : function () {
                return this.seconds();
            },
            S    : function () {
                return toInt(this.milliseconds() / 100);
            },
            SS   : function () {
                return leftZeroFill(toInt(this.milliseconds() / 10), 2);
            },
            SSS  : function () {
                return leftZeroFill(this.milliseconds(), 3);
            },
            SSSS : function () {
                return leftZeroFill(this.milliseconds(), 3);
            },
            Z    : function () {
                var a = this.utcOffset(),
                    b = '+';
                if (a < 0) {
                    a = -a;
                    b = '-';
                }
                return b + leftZeroFill(toInt(a / 60), 2) + ':' + leftZeroFill(toInt(a) % 60, 2);
            },
            ZZ   : function () {
                var a = this.utcOffset(),
                    b = '+';
                if (a < 0) {
                    a = -a;
                    b = '-';
                }
                return b + leftZeroFill(toInt(a / 60), 2) + leftZeroFill(toInt(a) % 60, 2);
            },
            z : function () {
                return this.zoneAbbr();
            },
            zz : function () {
                return this.zoneName();
            },
            x    : function () {
                return this.valueOf();
            },
            X    : function () {
                return this.unix();
            },
            Q : function () {
                return this.quarter();
            }
        },

        deprecations = {},

        lists = ['months', 'monthsShort', 'weekdays', 'weekdaysShort', 'weekdaysMin'],

        updateInProgress = false;

    // Pick the first defined of two or three arguments. dfl comes from
    // default.
    function dfl(a, b, c) {
        switch (arguments.length) {
            case 2: return a != null ? a : b;
            case 3: return a != null ? a : b != null ? b : c;
            default: throw new Error('Implement me');
        }
    }

    function hasOwnProp(a, b) {
        return hasOwnProperty.call(a, b);
    }

    function defaultParsingFlags() {
        // We need to deep clone this object, and es5 standard is not very
        // helpful.
        return {
            empty : false,
            unusedTokens : [],
            unusedInput : [],
            overflow : -2,
            charsLeftOver : 0,
            nullInput : false,
            invalidMonth : null,
            invalidFormat : false,
            userInvalidated : false,
            iso: false
        };
    }

    function printMsg(msg) {
        if (moment.suppressDeprecationWarnings === false &&
                typeof console !== 'undefined' && console.warn) {
            console.warn('Deprecation warning: ' + msg);
        }
    }

    function deprecate(msg, fn) {
        var firstTime = true;
        return extend(function () {
            if (firstTime) {
                printMsg(msg);
                firstTime = false;
            }
            return fn.apply(this, arguments);
        }, fn);
    }

    function deprecateSimple(name, msg) {
        if (!deprecations[name]) {
            printMsg(msg);
            deprecations[name] = true;
        }
    }

    function padToken(func, count) {
        return function (a) {
            return leftZeroFill(func.call(this, a), count);
        };
    }
    function ordinalizeToken(func, period) {
        return function (a) {
            return this.localeData().ordinal(func.call(this, a), period);
        };
    }

    function monthDiff(a, b) {
        // difference in months
        var wholeMonthDiff = ((b.year() - a.year()) * 12) + (b.month() - a.month()),
            // b is in (anchor - 1 month, anchor + 1 month)
            anchor = a.clone().add(wholeMonthDiff, 'months'),
            anchor2, adjust;

        if (b - anchor < 0) {
            anchor2 = a.clone().add(wholeMonthDiff - 1, 'months');
            // linear across the month
            adjust = (b - anchor) / (anchor - anchor2);
        } else {
            anchor2 = a.clone().add(wholeMonthDiff + 1, 'months');
            // linear across the month
            adjust = (b - anchor) / (anchor2 - anchor);
        }

        return -(wholeMonthDiff + adjust);
    }

    while (ordinalizeTokens.length) {
        i = ordinalizeTokens.pop();
        formatTokenFunctions[i + 'o'] = ordinalizeToken(formatTokenFunctions[i], i);
    }
    while (paddedTokens.length) {
        i = paddedTokens.pop();
        formatTokenFunctions[i + i] = padToken(formatTokenFunctions[i], 2);
    }
    formatTokenFunctions.DDDD = padToken(formatTokenFunctions.DDD, 3);


    function meridiemFixWrap(locale, hour, meridiem) {
        var isPm;

        if (meridiem == null) {
            // nothing to do
            return hour;
        }
        if (locale.meridiemHour != null) {
            return locale.meridiemHour(hour, meridiem);
        } else if (locale.isPM != null) {
            // Fallback
            isPm = locale.isPM(meridiem);
            if (isPm && hour < 12) {
                hour += 12;
            }
            if (!isPm && hour === 12) {
                hour = 0;
            }
            return hour;
        } else {
            // thie is not supposed to happen
            return hour;
        }
    }

    /************************************
        Constructors
    ************************************/

    function Locale() {
    }

    // Moment prototype object
    function Moment(config, skipOverflow) {
        if (skipOverflow !== false) {
            checkOverflow(config);
        }
        copyConfig(this, config);
        this._d = new Date(+config._d);
        // Prevent infinite loop in case updateOffset creates new moment
        // objects.
        if (updateInProgress === false) {
            updateInProgress = true;
            moment.updateOffset(this);
            updateInProgress = false;
        }
    }

    // Duration Constructor
    function Duration(duration) {
        var normalizedInput = normalizeObjectUnits(duration),
            years = normalizedInput.year || 0,
            quarters = normalizedInput.quarter || 0,
            months = normalizedInput.month || 0,
            weeks = normalizedInput.week || 0,
            days = normalizedInput.day || 0,
            hours = normalizedInput.hour || 0,
            minutes = normalizedInput.minute || 0,
            seconds = normalizedInput.second || 0,
            milliseconds = normalizedInput.millisecond || 0;

        // representation for dateAddRemove
        this._milliseconds = +milliseconds +
            seconds * 1e3 + // 1000
            minutes * 6e4 + // 1000 * 60
            hours * 36e5; // 1000 * 60 * 60
        // Because of dateAddRemove treats 24 hours as different from a
        // day when working around DST, we need to store them separately
        this._days = +days +
            weeks * 7;
        // It is impossible translate months into days without knowing
        // which months you are are talking about, so we have to store
        // it separately.
        this._months = +months +
            quarters * 3 +
            years * 12;

        this._data = {};

        this._locale = moment.localeData();

        this._bubble();
    }

    /************************************
        Helpers
    ************************************/


    function extend(a, b) {
        for (var i in b) {
            if (hasOwnProp(b, i)) {
                a[i] = b[i];
            }
        }

        if (hasOwnProp(b, 'toString')) {
            a.toString = b.toString;
        }

        if (hasOwnProp(b, 'valueOf')) {
            a.valueOf = b.valueOf;
        }

        return a;
    }

    function copyConfig(to, from) {
        var i, prop, val;

        if (typeof from._isAMomentObject !== 'undefined') {
            to._isAMomentObject = from._isAMomentObject;
        }
        if (typeof from._i !== 'undefined') {
            to._i = from._i;
        }
        if (typeof from._f !== 'undefined') {
            to._f = from._f;
        }
        if (typeof from._l !== 'undefined') {
            to._l = from._l;
        }
        if (typeof from._strict !== 'undefined') {
            to._strict = from._strict;
        }
        if (typeof from._tzm !== 'undefined') {
            to._tzm = from._tzm;
        }
        if (typeof from._isUTC !== 'undefined') {
            to._isUTC = from._isUTC;
        }
        if (typeof from._offset !== 'undefined') {
            to._offset = from._offset;
        }
        if (typeof from._pf !== 'undefined') {
            to._pf = from._pf;
        }
        if (typeof from._locale !== 'undefined') {
            to._locale = from._locale;
        }

        if (momentProperties.length > 0) {
            for (i in momentProperties) {
                prop = momentProperties[i];
                val = from[prop];
                if (typeof val !== 'undefined') {
                    to[prop] = val;
                }
            }
        }

        return to;
    }

    function absRound(number) {
        if (number < 0) {
            return Math.ceil(number);
        } else {
            return Math.floor(number);
        }
    }

    // left zero fill a number
    // see http://jsperf.com/left-zero-filling for performance comparison
    function leftZeroFill(number, targetLength, forceSign) {
        var output = '' + Math.abs(number),
            sign = number >= 0;

        while (output.length < targetLength) {
            output = '0' + output;
        }
        return (sign ? (forceSign ? '+' : '') : '-') + output;
    }

    function positiveMomentsDifference(base, other) {
        var res = {milliseconds: 0, months: 0};

        res.months = other.month() - base.month() +
            (other.year() - base.year()) * 12;
        if (base.clone().add(res.months, 'M').isAfter(other)) {
            --res.months;
        }

        res.milliseconds = +other - +(base.clone().add(res.months, 'M'));

        return res;
    }

    function momentsDifference(base, other) {
        var res;
        other = makeAs(other, base);
        if (base.isBefore(other)) {
            res = positiveMomentsDifference(base, other);
        } else {
            res = positiveMomentsDifference(other, base);
            res.milliseconds = -res.milliseconds;
            res.months = -res.months;
        }

        return res;
    }

    // TODO: remove 'name' arg after deprecation is removed
    function createAdder(direction, name) {
        return function (val, period) {
            var dur, tmp;
            //invert the arguments, but complain about it
            if (period !== null && !isNaN(+period)) {
                deprecateSimple(name, 'moment().' + name  + '(period, number) is deprecated. Please use moment().' + name + '(number, period).');
                tmp = val; val = period; period = tmp;
            }

            val = typeof val === 'string' ? +val : val;
            dur = moment.duration(val, period);
            addOrSubtractDurationFromMoment(this, dur, direction);
            return this;
        };
    }

    function addOrSubtractDurationFromMoment(mom, duration, isAdding, updateOffset) {
        var milliseconds = duration._milliseconds,
            days = duration._days,
            months = duration._months;
        updateOffset = updateOffset == null ? true : updateOffset;

        if (milliseconds) {
            mom._d.setTime(+mom._d + milliseconds * isAdding);
        }
        if (days) {
            rawSetter(mom, 'Date', rawGetter(mom, 'Date') + days * isAdding);
        }
        if (months) {
            rawMonthSetter(mom, rawGetter(mom, 'Month') + months * isAdding);
        }
        if (updateOffset) {
            moment.updateOffset(mom, days || months);
        }
    }

    // check if is an array
    function isArray(input) {
        return Object.prototype.toString.call(input) === '[object Array]';
    }

    function isDate(input) {
        return Object.prototype.toString.call(input) === '[object Date]' ||
            input instanceof Date;
    }

    // compare two arrays, return the number of differences
    function compareArrays(array1, array2, dontConvert) {
        var len = Math.min(array1.length, array2.length),
            lengthDiff = Math.abs(array1.length - array2.length),
            diffs = 0,
            i;
        for (i = 0; i < len; i++) {
            if ((dontConvert && array1[i] !== array2[i]) ||
                (!dontConvert && toInt(array1[i]) !== toInt(array2[i]))) {
                diffs++;
            }
        }
        return diffs + lengthDiff;
    }

    function normalizeUnits(units) {
        if (units) {
            var lowered = units.toLowerCase().replace(/(.)s$/, '$1');
            units = unitAliases[units] || camelFunctions[lowered] || lowered;
        }
        return units;
    }

    function normalizeObjectUnits(inputObject) {
        var normalizedInput = {},
            normalizedProp,
            prop;

        for (prop in inputObject) {
            if (hasOwnProp(inputObject, prop)) {
                normalizedProp = normalizeUnits(prop);
                if (normalizedProp) {
                    normalizedInput[normalizedProp] = inputObject[prop];
                }
            }
        }

        return normalizedInput;
    }

    function makeList(field) {
        var count, setter;

        if (field.indexOf('week') === 0) {
            count = 7;
            setter = 'day';
        }
        else if (field.indexOf('month') === 0) {
            count = 12;
            setter = 'month';
        }
        else {
            return;
        }

        moment[field] = function (format, index) {
            var i, getter,
                method = moment._locale[field],
                results = [];

            if (typeof format === 'number') {
                index = format;
                format = undefined;
            }

            getter = function (i) {
                var m = moment().utc().set(setter, i);
                return method.call(moment._locale, m, format || '');
            };

            if (index != null) {
                return getter(index);
            }
            else {
                for (i = 0; i < count; i++) {
                    results.push(getter(i));
                }
                return results;
            }
        };
    }

    function toInt(argumentForCoercion) {
        var coercedNumber = +argumentForCoercion,
            value = 0;

        if (coercedNumber !== 0 && isFinite(coercedNumber)) {
            if (coercedNumber >= 0) {
                value = Math.floor(coercedNumber);
            } else {
                value = Math.ceil(coercedNumber);
            }
        }

        return value;
    }

    function daysInMonth(year, month) {
        return new Date(Date.UTC(year, month + 1, 0)).getUTCDate();
    }

    function weeksInYear(year, dow, doy) {
        return weekOfYear(moment([year, 11, 31 + dow - doy]), dow, doy).week;
    }

    function daysInYear(year) {
        return isLeapYear(year) ? 366 : 365;
    }

    function isLeapYear(year) {
        return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
    }

    function checkOverflow(m) {
        var overflow;
        if (m._a && m._pf.overflow === -2) {
            overflow =
                m._a[MONTH] < 0 || m._a[MONTH] > 11 ? MONTH :
                m._a[DATE] < 1 || m._a[DATE] > daysInMonth(m._a[YEAR], m._a[MONTH]) ? DATE :
                m._a[HOUR] < 0 || m._a[HOUR] > 24 ||
                    (m._a[HOUR] === 24 && (m._a[MINUTE] !== 0 ||
                                           m._a[SECOND] !== 0 ||
                                           m._a[MILLISECOND] !== 0)) ? HOUR :
                m._a[MINUTE] < 0 || m._a[MINUTE] > 59 ? MINUTE :
                m._a[SECOND] < 0 || m._a[SECOND] > 59 ? SECOND :
                m._a[MILLISECOND] < 0 || m._a[MILLISECOND] > 999 ? MILLISECOND :
                -1;

            if (m._pf._overflowDayOfYear && (overflow < YEAR || overflow > DATE)) {
                overflow = DATE;
            }

            m._pf.overflow = overflow;
        }
    }

    function isValid(m) {
        if (m._isValid == null) {
            m._isValid = !isNaN(m._d.getTime()) &&
                m._pf.overflow < 0 &&
                !m._pf.empty &&
                !m._pf.invalidMonth &&
                !m._pf.nullInput &&
                !m._pf.invalidFormat &&
                !m._pf.userInvalidated;

            if (m._strict) {
                m._isValid = m._isValid &&
                    m._pf.charsLeftOver === 0 &&
                    m._pf.unusedTokens.length === 0 &&
                    m._pf.bigHour === undefined;
            }
        }
        return m._isValid;
    }

    function normalizeLocale(key) {
        return key ? key.toLowerCase().replace('_', '-') : key;
    }

    // pick the locale from the array
    // try ['en-au', 'en-gb'] as 'en-au', 'en-gb', 'en', as in move through the list trying each
    // substring from most specific to least, but move to the next array item if it's a more specific variant than the current root
    function chooseLocale(names) {
        var i = 0, j, next, locale, split;

        while (i < names.length) {
            split = normalizeLocale(names[i]).split('-');
            j = split.length;
            next = normalizeLocale(names[i + 1]);
            next = next ? next.split('-') : null;
            while (j > 0) {
                locale = loadLocale(split.slice(0, j).join('-'));
                if (locale) {
                    return locale;
                }
                if (next && next.length >= j && compareArrays(split, next, true) >= j - 1) {
                    //the next array item is better than a shallower substring of this one
                    break;
                }
                j--;
            }
            i++;
        }
        return null;
    }

    function loadLocale(name) {
        var oldLocale = null;
        if (!locales[name] && hasModule) {
            try {
                oldLocale = moment.locale();
                require('./locale/' + name);
                // because defineLocale currently also sets the global locale, we want to undo that for lazy loaded locales
                moment.locale(oldLocale);
            } catch (e) { }
        }
        return locales[name];
    }

    // Return a moment from input, that is local/utc/utcOffset equivalent to
    // model.
    function makeAs(input, model) {
        var res, diff;
        if (model._isUTC) {
            res = model.clone();
            diff = (moment.isMoment(input) || isDate(input) ?
                    +input : +moment(input)) - (+res);
            // Use low-level api, because this fn is low-level api.
            res._d.setTime(+res._d + diff);
            moment.updateOffset(res, false);
            return res;
        } else {
            return moment(input).local();
        }
    }

    /************************************
        Locale
    ************************************/


    extend(Locale.prototype, {

        set : function (config) {
            var prop, i;
            for (i in config) {
                prop = config[i];
                if (typeof prop === 'function') {
                    this[i] = prop;
                } else {
                    this['_' + i] = prop;
                }
            }
            // Lenient ordinal parsing accepts just a number in addition to
            // number + (possibly) stuff coming from _ordinalParseLenient.
            this._ordinalParseLenient = new RegExp(this._ordinalParse.source + '|' + /\d{1,2}/.source);
        },

        _months : 'January_February_March_April_May_June_July_August_September_October_November_December'.split('_'),
        months : function (m) {
            return this._months[m.month()];
        },

        _monthsShort : 'Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec'.split('_'),
        monthsShort : function (m) {
            return this._monthsShort[m.month()];
        },

        monthsParse : function (monthName, format, strict) {
            var i, mom, regex;

            if (!this._monthsParse) {
                this._monthsParse = [];
                this._longMonthsParse = [];
                this._shortMonthsParse = [];
            }

            for (i = 0; i < 12; i++) {
                // make the regex if we don't have it already
                mom = moment.utc([2000, i]);
                if (strict && !this._longMonthsParse[i]) {
                    this._longMonthsParse[i] = new RegExp('^' + this.months(mom, '').replace('.', '') + '$', 'i');
                    this._shortMonthsParse[i] = new RegExp('^' + this.monthsShort(mom, '').replace('.', '') + '$', 'i');
                }
                if (!strict && !this._monthsParse[i]) {
                    regex = '^' + this.months(mom, '') + '|^' + this.monthsShort(mom, '');
                    this._monthsParse[i] = new RegExp(regex.replace('.', ''), 'i');
                }
                // test the regex
                if (strict && format === 'MMMM' && this._longMonthsParse[i].test(monthName)) {
                    return i;
                } else if (strict && format === 'MMM' && this._shortMonthsParse[i].test(monthName)) {
                    return i;
                } else if (!strict && this._monthsParse[i].test(monthName)) {
                    return i;
                }
            }
        },

        _weekdays : 'Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday'.split('_'),
        weekdays : function (m) {
            return this._weekdays[m.day()];
        },

        _weekdaysShort : 'Sun_Mon_Tue_Wed_Thu_Fri_Sat'.split('_'),
        weekdaysShort : function (m) {
            return this._weekdaysShort[m.day()];
        },

        _weekdaysMin : 'Su_Mo_Tu_We_Th_Fr_Sa'.split('_'),
        weekdaysMin : function (m) {
            return this._weekdaysMin[m.day()];
        },

        weekdaysParse : function (weekdayName) {
            var i, mom, regex;

            if (!this._weekdaysParse) {
                this._weekdaysParse = [];
            }

            for (i = 0; i < 7; i++) {
                // make the regex if we don't have it already
                if (!this._weekdaysParse[i]) {
                    mom = moment([2000, 1]).day(i);
                    regex = '^' + this.weekdays(mom, '') + '|^' + this.weekdaysShort(mom, '') + '|^' + this.weekdaysMin(mom, '');
                    this._weekdaysParse[i] = new RegExp(regex.replace('.', ''), 'i');
                }
                // test the regex
                if (this._weekdaysParse[i].test(weekdayName)) {
                    return i;
                }
            }
        },

        _longDateFormat : {
            LTS : 'h:mm:ss A',
            LT : 'h:mm A',
            L : 'MM/DD/YYYY',
            LL : 'MMMM D, YYYY',
            LLL : 'MMMM D, YYYY LT',
            LLLL : 'dddd, MMMM D, YYYY LT'
        },
        longDateFormat : function (key) {
            var output = this._longDateFormat[key];
            if (!output && this._longDateFormat[key.toUpperCase()]) {
                output = this._longDateFormat[key.toUpperCase()].replace(/MMMM|MM|DD|dddd/g, function (val) {
                    return val.slice(1);
                });
                this._longDateFormat[key] = output;
            }
            return output;
        },

        isPM : function (input) {
            // IE8 Quirks Mode & IE7 Standards Mode do not allow accessing strings like arrays
            // Using charAt should be more compatible.
            return ((input + '').toLowerCase().charAt(0) === 'p');
        },

        _meridiemParse : /[ap]\.?m?\.?/i,
        meridiem : function (hours, minutes, isLower) {
            if (hours > 11) {
                return isLower ? 'pm' : 'PM';
            } else {
                return isLower ? 'am' : 'AM';
            }
        },


        _calendar : {
            sameDay : '[Today at] LT',
            nextDay : '[Tomorrow at] LT',
            nextWeek : 'dddd [at] LT',
            lastDay : '[Yesterday at] LT',
            lastWeek : '[Last] dddd [at] LT',
            sameElse : 'L'
        },
        calendar : function (key, mom, now) {
            var output = this._calendar[key];
            return typeof output === 'function' ? output.apply(mom, [now]) : output;
        },

        _relativeTime : {
            future : 'in %s',
            past : '%s ago',
            s : 'a few seconds',
            m : 'a minute',
            mm : '%d minutes',
            h : 'an hour',
            hh : '%d hours',
            d : 'a day',
            dd : '%d days',
            M : 'a month',
            MM : '%d months',
            y : 'a year',
            yy : '%d years'
        },

        relativeTime : function (number, withoutSuffix, string, isFuture) {
            var output = this._relativeTime[string];
            return (typeof output === 'function') ?
                output(number, withoutSuffix, string, isFuture) :
                output.replace(/%d/i, number);
        },

        pastFuture : function (diff, output) {
            var format = this._relativeTime[diff > 0 ? 'future' : 'past'];
            return typeof format === 'function' ? format(output) : format.replace(/%s/i, output);
        },

        ordinal : function (number) {
            return this._ordinal.replace('%d', number);
        },
        _ordinal : '%d',
        _ordinalParse : /\d{1,2}/,

        preparse : function (string) {
            return string;
        },

        postformat : function (string) {
            return string;
        },

        week : function (mom) {
            return weekOfYear(mom, this._week.dow, this._week.doy).week;
        },

        _week : {
            dow : 0, // Sunday is the first day of the week.
            doy : 6  // The week that contains Jan 1st is the first week of the year.
        },

        firstDayOfWeek : function () {
            return this._week.dow;
        },

        firstDayOfYear : function () {
            return this._week.doy;
        },

        _invalidDate: 'Invalid date',
        invalidDate: function () {
            return this._invalidDate;
        }
    });

    /************************************
        Formatting
    ************************************/


    function removeFormattingTokens(input) {
        if (input.match(/\[[\s\S]/)) {
            return input.replace(/^\[|\]$/g, '');
        }
        return input.replace(/\\/g, '');
    }

    function makeFormatFunction(format) {
        var array = format.match(formattingTokens), i, length;

        for (i = 0, length = array.length; i < length; i++) {
            if (formatTokenFunctions[array[i]]) {
                array[i] = formatTokenFunctions[array[i]];
            } else {
                array[i] = removeFormattingTokens(array[i]);
            }
        }

        return function (mom) {
            var output = '';
            for (i = 0; i < length; i++) {
                output += array[i] instanceof Function ? array[i].call(mom, format) : array[i];
            }
            return output;
        };
    }

    // format date using native date object
    function formatMoment(m, format) {
        if (!m.isValid()) {
            return m.localeData().invalidDate();
        }

        format = expandFormat(format, m.localeData());

        if (!formatFunctions[format]) {
            formatFunctions[format] = makeFormatFunction(format);
        }

        return formatFunctions[format](m);
    }

    function expandFormat(format, locale) {
        var i = 5;

        function replaceLongDateFormatTokens(input) {
            return locale.longDateFormat(input) || input;
        }

        localFormattingTokens.lastIndex = 0;
        while (i >= 0 && localFormattingTokens.test(format)) {
            format = format.replace(localFormattingTokens, replaceLongDateFormatTokens);
            localFormattingTokens.lastIndex = 0;
            i -= 1;
        }

        return format;
    }


    /************************************
        Parsing
    ************************************/


    // get the regex to find the next token
    function getParseRegexForToken(token, config) {
        var a, strict = config._strict;
        switch (token) {
        case 'Q':
            return parseTokenOneDigit;
        case 'DDDD':
            return parseTokenThreeDigits;
        case 'YYYY':
        case 'GGGG':
        case 'gggg':
            return strict ? parseTokenFourDigits : parseTokenOneToFourDigits;
        case 'Y':
        case 'G':
        case 'g':
            return parseTokenSignedNumber;
        case 'YYYYYY':
        case 'YYYYY':
        case 'GGGGG':
        case 'ggggg':
            return strict ? parseTokenSixDigits : parseTokenOneToSixDigits;
        case 'S':
            if (strict) {
                return parseTokenOneDigit;
            }
            /* falls through */
        case 'SS':
            if (strict) {
                return parseTokenTwoDigits;
            }
            /* falls through */
        case 'SSS':
            if (strict) {
                return parseTokenThreeDigits;
            }
            /* falls through */
        case 'DDD':
            return parseTokenOneToThreeDigits;
        case 'MMM':
        case 'MMMM':
        case 'dd':
        case 'ddd':
        case 'dddd':
            return parseTokenWord;
        case 'a':
        case 'A':
            return config._locale._meridiemParse;
        case 'x':
            return parseTokenOffsetMs;
        case 'X':
            return parseTokenTimestampMs;
        case 'Z':
        case 'ZZ':
            return parseTokenTimezone;
        case 'T':
            return parseTokenT;
        case 'SSSS':
            return parseTokenDigits;
        case 'MM':
        case 'DD':
        case 'YY':
        case 'GG':
        case 'gg':
        case 'HH':
        case 'hh':
        case 'mm':
        case 'ss':
        case 'ww':
        case 'WW':
            return strict ? parseTokenTwoDigits : parseTokenOneOrTwoDigits;
        case 'M':
        case 'D':
        case 'd':
        case 'H':
        case 'h':
        case 'm':
        case 's':
        case 'w':
        case 'W':
        case 'e':
        case 'E':
            return parseTokenOneOrTwoDigits;
        case 'Do':
            return strict ? config._locale._ordinalParse : config._locale._ordinalParseLenient;
        default :
            a = new RegExp(regexpEscape(unescapeFormat(token.replace('\\', '')), 'i'));
            return a;
        }
    }

    function utcOffsetFromString(string) {
        string = string || '';
        var possibleTzMatches = (string.match(parseTokenTimezone) || []),
            tzChunk = possibleTzMatches[possibleTzMatches.length - 1] || [],
            parts = (tzChunk + '').match(parseTimezoneChunker) || ['-', 0, 0],
            minutes = +(parts[1] * 60) + toInt(parts[2]);

        return parts[0] === '+' ? minutes : -minutes;
    }

    // function to convert string input to date
    function addTimeToArrayFromToken(token, input, config) {
        var a, datePartArray = config._a;

        switch (token) {
        // QUARTER
        case 'Q':
            if (input != null) {
                datePartArray[MONTH] = (toInt(input) - 1) * 3;
            }
            break;
        // MONTH
        case 'M' : // fall through to MM
        case 'MM' :
            if (input != null) {
                datePartArray[MONTH] = toInt(input) - 1;
            }
            break;
        case 'MMM' : // fall through to MMMM
        case 'MMMM' :
            a = config._locale.monthsParse(input, token, config._strict);
            // if we didn't find a month name, mark the date as invalid.
            if (a != null) {
                datePartArray[MONTH] = a;
            } else {
                config._pf.invalidMonth = input;
            }
            break;
        // DAY OF MONTH
        case 'D' : // fall through to DD
        case 'DD' :
            if (input != null) {
                datePartArray[DATE] = toInt(input);
            }
            break;
        case 'Do' :
            if (input != null) {
                datePartArray[DATE] = toInt(parseInt(
                            input.match(/\d{1,2}/)[0], 10));
            }
            break;
        // DAY OF YEAR
        case 'DDD' : // fall through to DDDD
        case 'DDDD' :
            if (input != null) {
                config._dayOfYear = toInt(input);
            }

            break;
        // YEAR
        case 'YY' :
            datePartArray[YEAR] = moment.parseTwoDigitYear(input);
            break;
        case 'YYYY' :
        case 'YYYYY' :
        case 'YYYYYY' :
            datePartArray[YEAR] = toInt(input);
            break;
        // AM / PM
        case 'a' : // fall through to A
        case 'A' :
            config._meridiem = input;
            // config._isPm = config._locale.isPM(input);
            break;
        // HOUR
        case 'h' : // fall through to hh
        case 'hh' :
            config._pf.bigHour = true;
            /* falls through */
        case 'H' : // fall through to HH
        case 'HH' :
            datePartArray[HOUR] = toInt(input);
            break;
        // MINUTE
        case 'm' : // fall through to mm
        case 'mm' :
            datePartArray[MINUTE] = toInt(input);
            break;
        // SECOND
        case 's' : // fall through to ss
        case 'ss' :
            datePartArray[SECOND] = toInt(input);
            break;
        // MILLISECOND
        case 'S' :
        case 'SS' :
        case 'SSS' :
        case 'SSSS' :
            datePartArray[MILLISECOND] = toInt(('0.' + input) * 1000);
            break;
        // UNIX OFFSET (MILLISECONDS)
        case 'x':
            config._d = new Date(toInt(input));
            break;
        // UNIX TIMESTAMP WITH MS
        case 'X':
            config._d = new Date(parseFloat(input) * 1000);
            break;
        // TIMEZONE
        case 'Z' : // fall through to ZZ
        case 'ZZ' :
            config._useUTC = true;
            config._tzm = utcOffsetFromString(input);
            break;
        // WEEKDAY - human
        case 'dd':
        case 'ddd':
        case 'dddd':
            a = config._locale.weekdaysParse(input);
            // if we didn't get a weekday name, mark the date as invalid
            if (a != null) {
                config._w = config._w || {};
                config._w['d'] = a;
            } else {
                config._pf.invalidWeekday = input;
            }
            break;
        // WEEK, WEEK DAY - numeric
        case 'w':
        case 'ww':
        case 'W':
        case 'WW':
        case 'd':
        case 'e':
        case 'E':
            token = token.substr(0, 1);
            /* falls through */
        case 'gggg':
        case 'GGGG':
        case 'GGGGG':
            token = token.substr(0, 2);
            if (input) {
                config._w = config._w || {};
                config._w[token] = toInt(input);
            }
            break;
        case 'gg':
        case 'GG':
            config._w = config._w || {};
            config._w[token] = moment.parseTwoDigitYear(input);
        }
    }

    function dayOfYearFromWeekInfo(config) {
        var w, weekYear, week, weekday, dow, doy, temp;

        w = config._w;
        if (w.GG != null || w.W != null || w.E != null) {
            dow = 1;
            doy = 4;

            // TODO: We need to take the current isoWeekYear, but that depends on
            // how we interpret now (local, utc, fixed offset). So create
            // a now version of current config (take local/utc/offset flags, and
            // create now).
            weekYear = dfl(w.GG, config._a[YEAR], weekOfYear(moment(), 1, 4).year);
            week = dfl(w.W, 1);
            weekday = dfl(w.E, 1);
        } else {
            dow = config._locale._week.dow;
            doy = config._locale._week.doy;

            weekYear = dfl(w.gg, config._a[YEAR], weekOfYear(moment(), dow, doy).year);
            week = dfl(w.w, 1);

            if (w.d != null) {
                // weekday -- low day numbers are considered next week
                weekday = w.d;
                if (weekday < dow) {
                    ++week;
                }
            } else if (w.e != null) {
                // local weekday -- counting starts from begining of week
                weekday = w.e + dow;
            } else {
                // default to begining of week
                weekday = dow;
            }
        }
        temp = dayOfYearFromWeeks(weekYear, week, weekday, doy, dow);

        config._a[YEAR] = temp.year;
        config._dayOfYear = temp.dayOfYear;
    }

    // convert an array to a date.
    // the array should mirror the parameters below
    // note: all values past the year are optional and will default to the lowest possible value.
    // [year, month, day , hour, minute, second, millisecond]
    function dateFromConfig(config) {
        var i, date, input = [], currentDate, yearToUse;

        if (config._d) {
            return;
        }

        currentDate = currentDateArray(config);

        //compute day of the year from weeks and weekdays
        if (config._w && config._a[DATE] == null && config._a[MONTH] == null) {
            dayOfYearFromWeekInfo(config);
        }

        //if the day of the year is set, figure out what it is
        if (config._dayOfYear) {
            yearToUse = dfl(config._a[YEAR], currentDate[YEAR]);

            if (config._dayOfYear > daysInYear(yearToUse)) {
                config._pf._overflowDayOfYear = true;
            }

            date = makeUTCDate(yearToUse, 0, config._dayOfYear);
            config._a[MONTH] = date.getUTCMonth();
            config._a[DATE] = date.getUTCDate();
        }

        // Default to current date.
        // * if no year, month, day of month are given, default to today
        // * if day of month is given, default month and year
        // * if month is given, default only year
        // * if year is given, don't default anything
        for (i = 0; i < 3 && config._a[i] == null; ++i) {
            config._a[i] = input[i] = currentDate[i];
        }

        // Zero out whatever was not defaulted, including time
        for (; i < 7; i++) {
            config._a[i] = input[i] = (config._a[i] == null) ? (i === 2 ? 1 : 0) : config._a[i];
        }

        // Check for 24:00:00.000
        if (config._a[HOUR] === 24 &&
                config._a[MINUTE] === 0 &&
                config._a[SECOND] === 0 &&
                config._a[MILLISECOND] === 0) {
            config._nextDay = true;
            config._a[HOUR] = 0;
        }

        config._d = (config._useUTC ? makeUTCDate : makeDate).apply(null, input);
        // Apply timezone offset from input. The actual utcOffset can be changed
        // with parseZone.
        if (config._tzm != null) {
            config._d.setUTCMinutes(config._d.getUTCMinutes() - config._tzm);
        }

        if (config._nextDay) {
            config._a[HOUR] = 24;
        }
    }

    function dateFromObject(config) {
        var normalizedInput;

        if (config._d) {
            return;
        }

        normalizedInput = normalizeObjectUnits(config._i);
        config._a = [
            normalizedInput.year,
            normalizedInput.month,
            normalizedInput.day || normalizedInput.date,
            normalizedInput.hour,
            normalizedInput.minute,
            normalizedInput.second,
            normalizedInput.millisecond
        ];

        dateFromConfig(config);
    }

    function currentDateArray(config) {
        var now = new Date();
        if (config._useUTC) {
            return [
                now.getUTCFullYear(),
                now.getUTCMonth(),
                now.getUTCDate()
            ];
        } else {
            return [now.getFullYear(), now.getMonth(), now.getDate()];
        }
    }

    // date from string and format string
    function makeDateFromStringAndFormat(config) {
        if (config._f === moment.ISO_8601) {
            parseISO(config);
            return;
        }

        config._a = [];
        config._pf.empty = true;

        // This array is used to make a Date, either with `new Date` or `Date.UTC`
        var string = '' + config._i,
            i, parsedInput, tokens, token, skipped,
            stringLength = string.length,
            totalParsedInputLength = 0;

        tokens = expandFormat(config._f, config._locale).match(formattingTokens) || [];

        for (i = 0; i < tokens.length; i++) {
            token = tokens[i];
            parsedInput = (string.match(getParseRegexForToken(token, config)) || [])[0];
            if (parsedInput) {
                skipped = string.substr(0, string.indexOf(parsedInput));
                if (skipped.length > 0) {
                    config._pf.unusedInput.push(skipped);
                }
                string = string.slice(string.indexOf(parsedInput) + parsedInput.length);
                totalParsedInputLength += parsedInput.length;
            }
            // don't parse if it's not a known token
            if (formatTokenFunctions[token]) {
                if (parsedInput) {
                    config._pf.empty = false;
                }
                else {
                    config._pf.unusedTokens.push(token);
                }
                addTimeToArrayFromToken(token, parsedInput, config);
            }
            else if (config._strict && !parsedInput) {
                config._pf.unusedTokens.push(token);
            }
        }

        // add remaining unparsed input length to the string
        config._pf.charsLeftOver = stringLength - totalParsedInputLength;
        if (string.length > 0) {
            config._pf.unusedInput.push(string);
        }

        // clear _12h flag if hour is <= 12
        if (config._pf.bigHour === true && config._a[HOUR] <= 12) {
            config._pf.bigHour = undefined;
        }
        // handle meridiem
        config._a[HOUR] = meridiemFixWrap(config._locale, config._a[HOUR],
                config._meridiem);
        dateFromConfig(config);
        checkOverflow(config);
    }

    function unescapeFormat(s) {
        return s.replace(/\\(\[)|\\(\])|\[([^\]\[]*)\]|\\(.)/g, function (matched, p1, p2, p3, p4) {
            return p1 || p2 || p3 || p4;
        });
    }

    // Code from http://stackoverflow.com/questions/3561493/is-there-a-regexp-escape-function-in-javascript
    function regexpEscape(s) {
        return s.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
    }

    // date from string and array of format strings
    function makeDateFromStringAndArray(config) {
        var tempConfig,
            bestMoment,

            scoreToBeat,
            i,
            currentScore;

        if (config._f.length === 0) {
            config._pf.invalidFormat = true;
            config._d = new Date(NaN);
            return;
        }

        for (i = 0; i < config._f.length; i++) {
            currentScore = 0;
            tempConfig = copyConfig({}, config);
            if (config._useUTC != null) {
                tempConfig._useUTC = config._useUTC;
            }
            tempConfig._pf = defaultParsingFlags();
            tempConfig._f = config._f[i];
            makeDateFromStringAndFormat(tempConfig);

            if (!isValid(tempConfig)) {
                continue;
            }

            // if there is any input that was not parsed add a penalty for that format
            currentScore += tempConfig._pf.charsLeftOver;

            //or tokens
            currentScore += tempConfig._pf.unusedTokens.length * 10;

            tempConfig._pf.score = currentScore;

            if (scoreToBeat == null || currentScore < scoreToBeat) {
                scoreToBeat = currentScore;
                bestMoment = tempConfig;
            }
        }

        extend(config, bestMoment || tempConfig);
    }

    // date from iso format
    function parseISO(config) {
        var i, l,
            string = config._i,
            match = isoRegex.exec(string);

        if (match) {
            config._pf.iso = true;
            for (i = 0, l = isoDates.length; i < l; i++) {
                if (isoDates[i][1].exec(string)) {
                    // match[5] should be 'T' or undefined
                    config._f = isoDates[i][0] + (match[6] || ' ');
                    break;
                }
            }
            for (i = 0, l = isoTimes.length; i < l; i++) {
                if (isoTimes[i][1].exec(string)) {
                    config._f += isoTimes[i][0];
                    break;
                }
            }
            if (string.match(parseTokenTimezone)) {
                config._f += 'Z';
            }
            makeDateFromStringAndFormat(config);
        } else {
            config._isValid = false;
        }
    }

    // date from iso format or fallback
    function makeDateFromString(config) {
        parseISO(config);
        if (config._isValid === false) {
            delete config._isValid;
            moment.createFromInputFallback(config);
        }
    }

    function map(arr, fn) {
        var res = [], i;
        for (i = 0; i < arr.length; ++i) {
            res.push(fn(arr[i], i));
        }
        return res;
    }

    function makeDateFromInput(config) {
        var input = config._i, matched;
        if (input === undefined) {
            config._d = new Date();
        } else if (isDate(input)) {
            config._d = new Date(+input);
        } else if ((matched = aspNetJsonRegex.exec(input)) !== null) {
            config._d = new Date(+matched[1]);
        } else if (typeof input === 'string') {
            makeDateFromString(config);
        } else if (isArray(input)) {
            config._a = map(input.slice(0), function (obj) {
                return parseInt(obj, 10);
            });
            dateFromConfig(config);
        } else if (typeof(input) === 'object') {
            dateFromObject(config);
        } else if (typeof(input) === 'number') {
            // from milliseconds
            config._d = new Date(input);
        } else {
            moment.createFromInputFallback(config);
        }
    }

    function makeDate(y, m, d, h, M, s, ms) {
        //can't just apply() to create a date:
        //http://stackoverflow.com/questions/181348/instantiating-a-javascript-object-by-calling-prototype-constructor-apply
        var date = new Date(y, m, d, h, M, s, ms);

        //the date constructor doesn't accept years < 1970
        if (y < 1970) {
            date.setFullYear(y);
        }
        return date;
    }

    function makeUTCDate(y) {
        var date = new Date(Date.UTC.apply(null, arguments));
        if (y < 1970) {
            date.setUTCFullYear(y);
        }
        return date;
    }

    function parseWeekday(input, locale) {
        if (typeof input === 'string') {
            if (!isNaN(input)) {
                input = parseInt(input, 10);
            }
            else {
                input = locale.weekdaysParse(input);
                if (typeof input !== 'number') {
                    return null;
                }
            }
        }
        return input;
    }

    /************************************
        Relative Time
    ************************************/


    // helper function for moment.fn.from, moment.fn.fromNow, and moment.duration.fn.humanize
    function substituteTimeAgo(string, number, withoutSuffix, isFuture, locale) {
        return locale.relativeTime(number || 1, !!withoutSuffix, string, isFuture);
    }

    function relativeTime(posNegDuration, withoutSuffix, locale) {
        var duration = moment.duration(posNegDuration).abs(),
            seconds = round(duration.as('s')),
            minutes = round(duration.as('m')),
            hours = round(duration.as('h')),
            days = round(duration.as('d')),
            months = round(duration.as('M')),
            years = round(duration.as('y')),

            args = seconds < relativeTimeThresholds.s && ['s', seconds] ||
                minutes === 1 && ['m'] ||
                minutes < relativeTimeThresholds.m && ['mm', minutes] ||
                hours === 1 && ['h'] ||
                hours < relativeTimeThresholds.h && ['hh', hours] ||
                days === 1 && ['d'] ||
                days < relativeTimeThresholds.d && ['dd', days] ||
                months === 1 && ['M'] ||
                months < relativeTimeThresholds.M && ['MM', months] ||
                years === 1 && ['y'] || ['yy', years];

        args[2] = withoutSuffix;
        args[3] = +posNegDuration > 0;
        args[4] = locale;
        return substituteTimeAgo.apply({}, args);
    }


    /************************************
        Week of Year
    ************************************/


    // firstDayOfWeek       0 = sun, 6 = sat
    //                      the day of the week that starts the week
    //                      (usually sunday or monday)
    // firstDayOfWeekOfYear 0 = sun, 6 = sat
    //                      the first week is the week that contains the first
    //                      of this day of the week
    //                      (eg. ISO weeks use thursday (4))
    function weekOfYear(mom, firstDayOfWeek, firstDayOfWeekOfYear) {
        var end = firstDayOfWeekOfYear - firstDayOfWeek,
            daysToDayOfWeek = firstDayOfWeekOfYear - mom.day(),
            adjustedMoment;


        if (daysToDayOfWeek > end) {
            daysToDayOfWeek -= 7;
        }

        if (daysToDayOfWeek < end - 7) {
            daysToDayOfWeek += 7;
        }

        adjustedMoment = moment(mom).add(daysToDayOfWeek, 'd');
        return {
            week: Math.ceil(adjustedMoment.dayOfYear() / 7),
            year: adjustedMoment.year()
        };
    }

    //http://en.wikipedia.org/wiki/ISO_week_date#Calculating_a_date_given_the_year.2C_week_number_and_weekday
    function dayOfYearFromWeeks(year, week, weekday, firstDayOfWeekOfYear, firstDayOfWeek) {
        var d = makeUTCDate(year, 0, 1).getUTCDay(), daysToAdd, dayOfYear;

        d = d === 0 ? 7 : d;
        weekday = weekday != null ? weekday : firstDayOfWeek;
        daysToAdd = firstDayOfWeek - d + (d > firstDayOfWeekOfYear ? 7 : 0) - (d < firstDayOfWeek ? 7 : 0);
        dayOfYear = 7 * (week - 1) + (weekday - firstDayOfWeek) + daysToAdd + 1;

        return {
            year: dayOfYear > 0 ? year : year - 1,
            dayOfYear: dayOfYear > 0 ?  dayOfYear : daysInYear(year - 1) + dayOfYear
        };
    }

    /************************************
        Top Level Functions
    ************************************/

    function makeMoment(config) {
        var input = config._i,
            format = config._f,
            res;

        config._locale = config._locale || moment.localeData(config._l);

        if (input === null || (format === undefined && input === '')) {
            return moment.invalid({nullInput: true});
        }

        if (typeof input === 'string') {
            config._i = input = config._locale.preparse(input);
        }

        if (moment.isMoment(input)) {
            return new Moment(input, true);
        } else if (format) {
            if (isArray(format)) {
                makeDateFromStringAndArray(config);
            } else {
                makeDateFromStringAndFormat(config);
            }
        } else {
            makeDateFromInput(config);
        }

        res = new Moment(config);
        if (res._nextDay) {
            // Adding is smart enough around DST
            res.add(1, 'd');
            res._nextDay = undefined;
        }

        return res;
    }

    moment = function (input, format, locale, strict) {
        var c;

        if (typeof(locale) === 'boolean') {
            strict = locale;
            locale = undefined;
        }
        // object construction must be done this way.
        // https://github.com/moment/moment/issues/1423
        c = {};
        c._isAMomentObject = true;
        c._i = input;
        c._f = format;
        c._l = locale;
        c._strict = strict;
        c._isUTC = false;
        c._pf = defaultParsingFlags();

        return makeMoment(c);
    };

    moment.suppressDeprecationWarnings = false;

    moment.createFromInputFallback = deprecate(
        'moment construction falls back to js Date. This is ' +
        'discouraged and will be removed in upcoming major ' +
        'release. Please refer to ' +
        'https://github.com/moment/moment/issues/1407 for more info.',
        function (config) {
            config._d = new Date(config._i + (config._useUTC ? ' UTC' : ''));
        }
    );

    // Pick a moment m from moments so that m[fn](other) is true for all
    // other. This relies on the function fn to be transitive.
    //
    // moments should either be an array of moment objects or an array, whose
    // first element is an array of moment objects.
    function pickBy(fn, moments) {
        var res, i;
        if (moments.length === 1 && isArray(moments[0])) {
            moments = moments[0];
        }
        if (!moments.length) {
            return moment();
        }
        res = moments[0];
        for (i = 1; i < moments.length; ++i) {
            if (moments[i][fn](res)) {
                res = moments[i];
            }
        }
        return res;
    }

    moment.min = function () {
        var args = [].slice.call(arguments, 0);

        return pickBy('isBefore', args);
    };

    moment.max = function () {
        var args = [].slice.call(arguments, 0);

        return pickBy('isAfter', args);
    };

    // creating with utc
    moment.utc = function (input, format, locale, strict) {
        var c;

        if (typeof(locale) === 'boolean') {
            strict = locale;
            locale = undefined;
        }
        // object construction must be done this way.
        // https://github.com/moment/moment/issues/1423
        c = {};
        c._isAMomentObject = true;
        c._useUTC = true;
        c._isUTC = true;
        c._l = locale;
        c._i = input;
        c._f = format;
        c._strict = strict;
        c._pf = defaultParsingFlags();

        return makeMoment(c).utc();
    };

    // creating with unix timestamp (in seconds)
    moment.unix = function (input) {
        return moment(input * 1000);
    };

    // duration
    moment.duration = function (input, key) {
        var duration = input,
            // matching against regexp is expensive, do it on demand
            match = null,
            sign,
            ret,
            parseIso,
            diffRes;

        if (moment.isDuration(input)) {
            duration = {
                ms: input._milliseconds,
                d: input._days,
                M: input._months
            };
        } else if (typeof input === 'number') {
            duration = {};
            if (key) {
                duration[key] = input;
            } else {
                duration.milliseconds = input;
            }
        } else if (!!(match = aspNetTimeSpanJsonRegex.exec(input))) {
            sign = (match[1] === '-') ? -1 : 1;
            duration = {
                y: 0,
                d: toInt(match[DATE]) * sign,
                h: toInt(match[HOUR]) * sign,
                m: toInt(match[MINUTE]) * sign,
                s: toInt(match[SECOND]) * sign,
                ms: toInt(match[MILLISECOND]) * sign
            };
        } else if (!!(match = isoDurationRegex.exec(input))) {
            sign = (match[1] === '-') ? -1 : 1;
            parseIso = function (inp) {
                // We'd normally use ~~inp for this, but unfortunately it also
                // converts floats to ints.
                // inp may be undefined, so careful calling replace on it.
                var res = inp && parseFloat(inp.replace(',', '.'));
                // apply sign while we're at it
                return (isNaN(res) ? 0 : res) * sign;
            };
            duration = {
                y: parseIso(match[2]),
                M: parseIso(match[3]),
                d: parseIso(match[4]),
                h: parseIso(match[5]),
                m: parseIso(match[6]),
                s: parseIso(match[7]),
                w: parseIso(match[8])
            };
        } else if (duration == null) {// checks for null or undefined
            duration = {};
        } else if (typeof duration === 'object' &&
                ('from' in duration || 'to' in duration)) {
            diffRes = momentsDifference(moment(duration.from), moment(duration.to));

            duration = {};
            duration.ms = diffRes.milliseconds;
            duration.M = diffRes.months;
        }

        ret = new Duration(duration);

        if (moment.isDuration(input) && hasOwnProp(input, '_locale')) {
            ret._locale = input._locale;
        }

        return ret;
    };

    // version number
    moment.version = VERSION;

    // default format
    moment.defaultFormat = isoFormat;

    // constant that refers to the ISO standard
    moment.ISO_8601 = function () {};

    // Plugins that add properties should also add the key here (null value),
    // so we can properly clone ourselves.
    moment.momentProperties = momentProperties;

    // This function will be called whenever a moment is mutated.
    // It is intended to keep the offset in sync with the timezone.
    moment.updateOffset = function () {};

    // This function allows you to set a threshold for relative time strings
    moment.relativeTimeThreshold = function (threshold, limit) {
        if (relativeTimeThresholds[threshold] === undefined) {
            return false;
        }
        if (limit === undefined) {
            return relativeTimeThresholds[threshold];
        }
        relativeTimeThresholds[threshold] = limit;
        return true;
    };

    moment.lang = deprecate(
        'moment.lang is deprecated. Use moment.locale instead.',
        function (key, value) {
            return moment.locale(key, value);
        }
    );

    // This function will load locale and then set the global locale.  If
    // no arguments are passed in, it will simply return the current global
    // locale key.
    moment.locale = function (key, values) {
        var data;
        if (key) {
            if (typeof(values) !== 'undefined') {
                data = moment.defineLocale(key, values);
            }
            else {
                data = moment.localeData(key);
            }

            if (data) {
                moment.duration._locale = moment._locale = data;
            }
        }

        return moment._locale._abbr;
    };

    moment.defineLocale = function (name, values) {
        if (values !== null) {
            values.abbr = name;
            if (!locales[name]) {
                locales[name] = new Locale();
            }
            locales[name].set(values);

            // backwards compat for now: also set the locale
            moment.locale(name);

            return locales[name];
        } else {
            // useful for testing
            delete locales[name];
            return null;
        }
    };

    moment.langData = deprecate(
        'moment.langData is deprecated. Use moment.localeData instead.',
        function (key) {
            return moment.localeData(key);
        }
    );

    // returns locale data
    moment.localeData = function (key) {
        var locale;

        if (key && key._locale && key._locale._abbr) {
            key = key._locale._abbr;
        }

        if (!key) {
            return moment._locale;
        }

        if (!isArray(key)) {
            //short-circuit everything else
            locale = loadLocale(key);
            if (locale) {
                return locale;
            }
            key = [key];
        }

        return chooseLocale(key);
    };

    // compare moment object
    moment.isMoment = function (obj) {
        return obj instanceof Moment ||
            (obj != null && hasOwnProp(obj, '_isAMomentObject'));
    };

    // for typechecking Duration objects
    moment.isDuration = function (obj) {
        return obj instanceof Duration;
    };

    for (i = lists.length - 1; i >= 0; --i) {
        makeList(lists[i]);
    }

    moment.normalizeUnits = function (units) {
        return normalizeUnits(units);
    };

    moment.invalid = function (flags) {
        var m = moment.utc(NaN);
        if (flags != null) {
            extend(m._pf, flags);
        }
        else {
            m._pf.userInvalidated = true;
        }

        return m;
    };

    moment.parseZone = function () {
        return moment.apply(null, arguments).parseZone();
    };

    moment.parseTwoDigitYear = function (input) {
        return toInt(input) + (toInt(input) > 68 ? 1900 : 2000);
    };

    moment.isDate = isDate;

    /************************************
        Moment Prototype
    ************************************/


    extend(moment.fn = Moment.prototype, {

        clone : function () {
            return moment(this);
        },

        valueOf : function () {
            return +this._d - ((this._offset || 0) * 60000);
        },

        unix : function () {
            return Math.floor(+this / 1000);
        },

        toString : function () {
            return this.clone().locale('en').format('ddd MMM DD YYYY HH:mm:ss [GMT]ZZ');
        },

        toDate : function () {
            return this._offset ? new Date(+this) : this._d;
        },

        toISOString : function () {
            var m = moment(this).utc();
            if (0 < m.year() && m.year() <= 9999) {
                if ('function' === typeof Date.prototype.toISOString) {
                    // native implementation is ~50x faster, use it when we can
                    return this.toDate().toISOString();
                } else {
                    return formatMoment(m, 'YYYY-MM-DD[T]HH:mm:ss.SSS[Z]');
                }
            } else {
                return formatMoment(m, 'YYYYYY-MM-DD[T]HH:mm:ss.SSS[Z]');
            }
        },

        toArray : function () {
            var m = this;
            return [
                m.year(),
                m.month(),
                m.date(),
                m.hours(),
                m.minutes(),
                m.seconds(),
                m.milliseconds()
            ];
        },

        isValid : function () {
            return isValid(this);
        },

        isDSTShifted : function () {
            if (this._a) {
                return this.isValid() && compareArrays(this._a, (this._isUTC ? moment.utc(this._a) : moment(this._a)).toArray()) > 0;
            }

            return false;
        },

        parsingFlags : function () {
            return extend({}, this._pf);
        },

        invalidAt: function () {
            return this._pf.overflow;
        },

        utc : function (keepLocalTime) {
            return this.utcOffset(0, keepLocalTime);
        },

        local : function (keepLocalTime) {
            if (this._isUTC) {
                this.utcOffset(0, keepLocalTime);
                this._isUTC = false;

                if (keepLocalTime) {
                    this.subtract(this._dateUtcOffset(), 'm');
                }
            }
            return this;
        },

        format : function (inputString) {
            var output = formatMoment(this, inputString || moment.defaultFormat);
            return this.localeData().postformat(output);
        },

        add : createAdder(1, 'add'),

        subtract : createAdder(-1, 'subtract'),

        diff : function (input, units, asFloat) {
            var that = makeAs(input, this),
                zoneDiff = (that.utcOffset() - this.utcOffset()) * 6e4,
                anchor, diff, output, daysAdjust;

            units = normalizeUnits(units);

            if (units === 'year' || units === 'month' || units === 'quarter') {
                output = monthDiff(this, that);
                if (units === 'quarter') {
                    output = output / 3;
                } else if (units === 'year') {
                    output = output / 12;
                }
            } else {
                diff = this - that;
                output = units === 'second' ? diff / 1e3 : // 1000
                    units === 'minute' ? diff / 6e4 : // 1000 * 60
                    units === 'hour' ? diff / 36e5 : // 1000 * 60 * 60
                    units === 'day' ? (diff - zoneDiff) / 864e5 : // 1000 * 60 * 60 * 24, negate dst
                    units === 'week' ? (diff - zoneDiff) / 6048e5 : // 1000 * 60 * 60 * 24 * 7, negate dst
                    diff;
            }
            return asFloat ? output : absRound(output);
        },

        from : function (time, withoutSuffix) {
            return moment.duration({to: this, from: time}).locale(this.locale()).humanize(!withoutSuffix);
        },

        fromNow : function (withoutSuffix) {
            return this.from(moment(), withoutSuffix);
        },

        calendar : function (time) {
            // We want to compare the start of today, vs this.
            // Getting start-of-today depends on whether we're locat/utc/offset
            // or not.
            var now = time || moment(),
                sod = makeAs(now, this).startOf('day'),
                diff = this.diff(sod, 'days', true),
                format = diff < -6 ? 'sameElse' :
                    diff < -1 ? 'lastWeek' :
                    diff < 0 ? 'lastDay' :
                    diff < 1 ? 'sameDay' :
                    diff < 2 ? 'nextDay' :
                    diff < 7 ? 'nextWeek' : 'sameElse';
            return this.format(this.localeData().calendar(format, this, moment(now)));
        },

        isLeapYear : function () {
            return isLeapYear(this.year());
        },

        isDST : function () {
            return (this.utcOffset() > this.clone().month(0).utcOffset() ||
                this.utcOffset() > this.clone().month(5).utcOffset());
        },

        day : function (input) {
            var day = this._isUTC ? this._d.getUTCDay() : this._d.getDay();
            if (input != null) {
                input = parseWeekday(input, this.localeData());
                return this.add(input - day, 'd');
            } else {
                return day;
            }
        },

        month : makeAccessor('Month', true),

        startOf : function (units) {
            units = normalizeUnits(units);
            // the following switch intentionally omits break keywords
            // to utilize falling through the cases.
            switch (units) {
            case 'year':
                this.month(0);
                /* falls through */
            case 'quarter':
            case 'month':
                this.date(1);
                /* falls through */
            case 'week':
            case 'isoWeek':
            case 'day':
                this.hours(0);
                /* falls through */
            case 'hour':
                this.minutes(0);
                /* falls through */
            case 'minute':
                this.seconds(0);
                /* falls through */
            case 'second':
                this.milliseconds(0);
                /* falls through */
            }

            // weeks are a special case
            if (units === 'week') {
                this.weekday(0);
            } else if (units === 'isoWeek') {
                this.isoWeekday(1);
            }

            // quarters are also special
            if (units === 'quarter') {
                this.month(Math.floor(this.month() / 3) * 3);
            }

            return this;
        },

        endOf: function (units) {
            units = normalizeUnits(units);
            if (units === undefined || units === 'millisecond') {
                return this;
            }
            return this.startOf(units).add(1, (units === 'isoWeek' ? 'week' : units)).subtract(1, 'ms');
        },

        isAfter: function (input, units) {
            var inputMs;
            units = normalizeUnits(typeof units !== 'undefined' ? units : 'millisecond');
            if (units === 'millisecond') {
                input = moment.isMoment(input) ? input : moment(input);
                return +this > +input;
            } else {
                inputMs = moment.isMoment(input) ? +input : +moment(input);
                return inputMs < +this.clone().startOf(units);
            }
        },

        isBefore: function (input, units) {
            var inputMs;
            units = normalizeUnits(typeof units !== 'undefined' ? units : 'millisecond');
            if (units === 'millisecond') {
                input = moment.isMoment(input) ? input : moment(input);
                return +this < +input;
            } else {
                inputMs = moment.isMoment(input) ? +input : +moment(input);
                return +this.clone().endOf(units) < inputMs;
            }
        },

        isBetween: function (from, to, units) {
            return this.isAfter(from, units) && this.isBefore(to, units);
        },

        isSame: function (input, units) {
            var inputMs;
            units = normalizeUnits(units || 'millisecond');
            if (units === 'millisecond') {
                input = moment.isMoment(input) ? input : moment(input);
                return +this === +input;
            } else {
                inputMs = +moment(input);
                return +(this.clone().startOf(units)) <= inputMs && inputMs <= +(this.clone().endOf(units));
            }
        },

        min: deprecate(
                 'moment().min is deprecated, use moment.min instead. https://github.com/moment/moment/issues/1548',
                 function (other) {
                     other = moment.apply(null, arguments);
                     return other < this ? this : other;
                 }
         ),

        max: deprecate(
                'moment().max is deprecated, use moment.max instead. https://github.com/moment/moment/issues/1548',
                function (other) {
                    other = moment.apply(null, arguments);
                    return other > this ? this : other;
                }
        ),

        zone : deprecate(
                'moment().zone is deprecated, use moment().utcOffset instead. ' +
                'https://github.com/moment/moment/issues/1779',
                function (input, keepLocalTime) {
                    if (input != null) {
                        if (typeof input !== 'string') {
                            input = -input;
                        }

                        this.utcOffset(input, keepLocalTime);

                        return this;
                    } else {
                        return -this.utcOffset();
                    }
                }
        ),

        // keepLocalTime = true means only change the timezone, without
        // affecting the local hour. So 5:31:26 +0300 --[utcOffset(2, true)]-->
        // 5:31:26 +0200 It is possible that 5:31:26 doesn't exist with offset
        // +0200, so we adjust the time as needed, to be valid.
        //
        // Keeping the time actually adds/subtracts (one hour)
        // from the actual represented time. That is why we call updateOffset
        // a second time. In case it wants us to change the offset again
        // _changeInProgress == true case, then we have to adjust, because
        // there is no such time in the given timezone.
        utcOffset : function (input, keepLocalTime) {
            var offset = this._offset || 0,
                localAdjust;
            if (input != null) {
                if (typeof input === 'string') {
                    input = utcOffsetFromString(input);
                }
                if (Math.abs(input) < 16) {
                    input = input * 60;
                }
                if (!this._isUTC && keepLocalTime) {
                    localAdjust = this._dateUtcOffset();
                }
                this._offset = input;
                this._isUTC = true;
                if (localAdjust != null) {
                    this.add(localAdjust, 'm');
                }
                if (offset !== input) {
                    if (!keepLocalTime || this._changeInProgress) {
                        addOrSubtractDurationFromMoment(this,
                                moment.duration(input - offset, 'm'), 1, false);
                    } else if (!this._changeInProgress) {
                        this._changeInProgress = true;
                        moment.updateOffset(this, true);
                        this._changeInProgress = null;
                    }
                }

                return this;
            } else {
                return this._isUTC ? offset : this._dateUtcOffset();
            }
        },

        isLocal : function () {
            return !this._isUTC;
        },

        isUtcOffset : function () {
            return this._isUTC;
        },

        isUtc : function () {
            return this._isUTC && this._offset === 0;
        },

        zoneAbbr : function () {
            return this._isUTC ? 'UTC' : '';
        },

        zoneName : function () {
            return this._isUTC ? 'Coordinated Universal Time' : '';
        },

        parseZone : function () {
            if (this._tzm) {
                this.utcOffset(this._tzm);
            } else if (typeof this._i === 'string') {
                this.utcOffset(utcOffsetFromString(this._i));
            }
            return this;
        },

        hasAlignedHourOffset : function (input) {
            if (!input) {
                input = 0;
            }
            else {
                input = moment(input).utcOffset();
            }

            return (this.utcOffset() - input) % 60 === 0;
        },

        daysInMonth : function () {
            return daysInMonth(this.year(), this.month());
        },

        dayOfYear : function (input) {
            var dayOfYear = round((moment(this).startOf('day') - moment(this).startOf('year')) / 864e5) + 1;
            return input == null ? dayOfYear : this.add((input - dayOfYear), 'd');
        },

        quarter : function (input) {
            return input == null ? Math.ceil((this.month() + 1) / 3) : this.month((input - 1) * 3 + this.month() % 3);
        },

        weekYear : function (input) {
            var year = weekOfYear(this, this.localeData()._week.dow, this.localeData()._week.doy).year;
            return input == null ? year : this.add((input - year), 'y');
        },

        isoWeekYear : function (input) {
            var year = weekOfYear(this, 1, 4).year;
            return input == null ? year : this.add((input - year), 'y');
        },

        week : function (input) {
            var week = this.localeData().week(this);
            return input == null ? week : this.add((input - week) * 7, 'd');
        },

        isoWeek : function (input) {
            var week = weekOfYear(this, 1, 4).week;
            return input == null ? week : this.add((input - week) * 7, 'd');
        },

        weekday : function (input) {
            var weekday = (this.day() + 7 - this.localeData()._week.dow) % 7;
            return input == null ? weekday : this.add(input - weekday, 'd');
        },

        isoWeekday : function (input) {
            // behaves the same as moment#day except
            // as a getter, returns 7 instead of 0 (1-7 range instead of 0-6)
            // as a setter, sunday should belong to the previous week.
            return input == null ? this.day() || 7 : this.day(this.day() % 7 ? input : input - 7);
        },

        isoWeeksInYear : function () {
            return weeksInYear(this.year(), 1, 4);
        },

        weeksInYear : function () {
            var weekInfo = this.localeData()._week;
            return weeksInYear(this.year(), weekInfo.dow, weekInfo.doy);
        },

        get : function (units) {
            units = normalizeUnits(units);
            return this[units]();
        },

        set : function (units, value) {
            var unit;
            if (typeof units === 'object') {
                for (unit in units) {
                    this.set(unit, units[unit]);
                }
            }
            else {
                units = normalizeUnits(units);
                if (typeof this[units] === 'function') {
                    this[units](value);
                }
            }
            return this;
        },

        // If passed a locale key, it will set the locale for this
        // instance.  Otherwise, it will return the locale configuration
        // variables for this instance.
        locale : function (key) {
            var newLocaleData;

            if (key === undefined) {
                return this._locale._abbr;
            } else {
                newLocaleData = moment.localeData(key);
                if (newLocaleData != null) {
                    this._locale = newLocaleData;
                }
                return this;
            }
        },

        lang : deprecate(
            'moment().lang() is deprecated. Instead, use moment().localeData() to get the language configuration. Use moment().locale() to change language.',
            function (key) {
                if (key === undefined) {
                    return this.localeData();
                } else {
                    return this.locale(key);
                }
            }
        ),

        localeData : function () {
            return this._locale;
        },

        _dateUtcOffset : function () {
            // On Firefox.24 Date#getTimezoneOffset returns a floating point.
            // https://github.com/moment/moment/pull/1871
            return -Math.round(this._d.getTimezoneOffset() / 15) * 15;
        }

    });

    function rawMonthSetter(mom, value) {
        var dayOfMonth;

        // TODO: Move this out of here!
        if (typeof value === 'string') {
            value = mom.localeData().monthsParse(value);
            // TODO: Another silent failure?
            if (typeof value !== 'number') {
                return mom;
            }
        }

        dayOfMonth = Math.min(mom.date(),
                daysInMonth(mom.year(), value));
        mom._d['set' + (mom._isUTC ? 'UTC' : '') + 'Month'](value, dayOfMonth);
        return mom;
    }

    function rawGetter(mom, unit) {
        return mom._d['get' + (mom._isUTC ? 'UTC' : '') + unit]();
    }

    function rawSetter(mom, unit, value) {
        if (unit === 'Month') {
            return rawMonthSetter(mom, value);
        } else {
            return mom._d['set' + (mom._isUTC ? 'UTC' : '') + unit](value);
        }
    }

    function makeAccessor(unit, keepTime) {
        return function (value) {
            if (value != null) {
                rawSetter(this, unit, value);
                moment.updateOffset(this, keepTime);
                return this;
            } else {
                return rawGetter(this, unit);
            }
        };
    }

    moment.fn.millisecond = moment.fn.milliseconds = makeAccessor('Milliseconds', false);
    moment.fn.second = moment.fn.seconds = makeAccessor('Seconds', false);
    moment.fn.minute = moment.fn.minutes = makeAccessor('Minutes', false);
    // Setting the hour should keep the time, because the user explicitly
    // specified which hour he wants. So trying to maintain the same hour (in
    // a new timezone) makes sense. Adding/subtracting hours does not follow
    // this rule.
    moment.fn.hour = moment.fn.hours = makeAccessor('Hours', true);
    // moment.fn.month is defined separately
    moment.fn.date = makeAccessor('Date', true);
    moment.fn.dates = deprecate('dates accessor is deprecated. Use date instead.', makeAccessor('Date', true));
    moment.fn.year = makeAccessor('FullYear', true);
    moment.fn.years = deprecate('years accessor is deprecated. Use year instead.', makeAccessor('FullYear', true));

    // add plural methods
    moment.fn.days = moment.fn.day;
    moment.fn.months = moment.fn.month;
    moment.fn.weeks = moment.fn.week;
    moment.fn.isoWeeks = moment.fn.isoWeek;
    moment.fn.quarters = moment.fn.quarter;

    // add aliased format methods
    moment.fn.toJSON = moment.fn.toISOString;

    // alias isUtc for dev-friendliness
    moment.fn.isUTC = moment.fn.isUtc;

    /************************************
        Duration Prototype
    ************************************/


    function daysToYears (days) {
        // 400 years have 146097 days (taking into account leap year rules)
        return days * 400 / 146097;
    }

    function yearsToDays (years) {
        // years * 365 + absRound(years / 4) -
        //     absRound(years / 100) + absRound(years / 400);
        return years * 146097 / 400;
    }

    extend(moment.duration.fn = Duration.prototype, {

        _bubble : function () {
            var milliseconds = this._milliseconds,
                days = this._days,
                months = this._months,
                data = this._data,
                seconds, minutes, hours, years = 0;

            // The following code bubbles up values, see the tests for
            // examples of what that means.
            data.milliseconds = milliseconds % 1000;

            seconds = absRound(milliseconds / 1000);
            data.seconds = seconds % 60;

            minutes = absRound(seconds / 60);
            data.minutes = minutes % 60;

            hours = absRound(minutes / 60);
            data.hours = hours % 24;

            days += absRound(hours / 24);

            // Accurately convert days to years, assume start from year 0.
            years = absRound(daysToYears(days));
            days -= absRound(yearsToDays(years));

            // 30 days to a month
            // TODO (iskren): Use anchor date (like 1st Jan) to compute this.
            months += absRound(days / 30);
            days %= 30;

            // 12 months -> 1 year
            years += absRound(months / 12);
            months %= 12;

            data.days = days;
            data.months = months;
            data.years = years;
        },

        abs : function () {
            this._milliseconds = Math.abs(this._milliseconds);
            this._days = Math.abs(this._days);
            this._months = Math.abs(this._months);

            this._data.milliseconds = Math.abs(this._data.milliseconds);
            this._data.seconds = Math.abs(this._data.seconds);
            this._data.minutes = Math.abs(this._data.minutes);
            this._data.hours = Math.abs(this._data.hours);
            this._data.months = Math.abs(this._data.months);
            this._data.years = Math.abs(this._data.years);

            return this;
        },

        weeks : function () {
            return absRound(this.days() / 7);
        },

        valueOf : function () {
            return this._milliseconds +
              this._days * 864e5 +
              (this._months % 12) * 2592e6 +
              toInt(this._months / 12) * 31536e6;
        },

        humanize : function (withSuffix) {
            var output = relativeTime(this, !withSuffix, this.localeData());

            if (withSuffix) {
                output = this.localeData().pastFuture(+this, output);
            }

            return this.localeData().postformat(output);
        },

        add : function (input, val) {
            // supports only 2.0-style add(1, 's') or add(moment)
            var dur = moment.duration(input, val);

            this._milliseconds += dur._milliseconds;
            this._days += dur._days;
            this._months += dur._months;

            this._bubble();

            return this;
        },

        subtract : function (input, val) {
            var dur = moment.duration(input, val);

            this._milliseconds -= dur._milliseconds;
            this._days -= dur._days;
            this._months -= dur._months;

            this._bubble();

            return this;
        },

        get : function (units) {
            units = normalizeUnits(units);
            return this[units.toLowerCase() + 's']();
        },

        as : function (units) {
            var days, months;
            units = normalizeUnits(units);

            if (units === 'month' || units === 'year') {
                days = this._days + this._milliseconds / 864e5;
                months = this._months + daysToYears(days) * 12;
                return units === 'month' ? months : months / 12;
            } else {
                // handle milliseconds separately because of floating point math errors (issue #1867)
                days = this._days + Math.round(yearsToDays(this._months / 12));
                switch (units) {
                    case 'week': return days / 7 + this._milliseconds / 6048e5;
                    case 'day': return days + this._milliseconds / 864e5;
                    case 'hour': return days * 24 + this._milliseconds / 36e5;
                    case 'minute': return days * 24 * 60 + this._milliseconds / 6e4;
                    case 'second': return days * 24 * 60 * 60 + this._milliseconds / 1000;
                    // Math.floor prevents floating point math errors here
                    case 'millisecond': return Math.floor(days * 24 * 60 * 60 * 1000) + this._milliseconds;
                    default: throw new Error('Unknown unit ' + units);
                }
            }
        },

        lang : moment.fn.lang,
        locale : moment.fn.locale,

        toIsoString : deprecate(
            'toIsoString() is deprecated. Please use toISOString() instead ' +
            '(notice the capitals)',
            function () {
                return this.toISOString();
            }
        ),

        toISOString : function () {
            // inspired by https://github.com/dordille/moment-isoduration/blob/master/moment.isoduration.js
            var years = Math.abs(this.years()),
                months = Math.abs(this.months()),
                days = Math.abs(this.days()),
                hours = Math.abs(this.hours()),
                minutes = Math.abs(this.minutes()),
                seconds = Math.abs(this.seconds() + this.milliseconds() / 1000);

            if (!this.asSeconds()) {
                // this is the same as C#'s (Noda) and python (isodate)...
                // but not other JS (goog.date)
                return 'P0D';
            }

            return (this.asSeconds() < 0 ? '-' : '') +
                'P' +
                (years ? years + 'Y' : '') +
                (months ? months + 'M' : '') +
                (days ? days + 'D' : '') +
                ((hours || minutes || seconds) ? 'T' : '') +
                (hours ? hours + 'H' : '') +
                (minutes ? minutes + 'M' : '') +
                (seconds ? seconds + 'S' : '');
        },

        localeData : function () {
            return this._locale;
        },

        toJSON : function () {
            return this.toISOString();
        }
    });

    moment.duration.fn.toString = moment.duration.fn.toISOString;

    function makeDurationGetter(name) {
        moment.duration.fn[name] = function () {
            return this._data[name];
        };
    }

    for (i in unitMillisecondFactors) {
        if (hasOwnProp(unitMillisecondFactors, i)) {
            makeDurationGetter(i.toLowerCase());
        }
    }

    moment.duration.fn.asMilliseconds = function () {
        return this.as('ms');
    };
    moment.duration.fn.asSeconds = function () {
        return this.as('s');
    };
    moment.duration.fn.asMinutes = function () {
        return this.as('m');
    };
    moment.duration.fn.asHours = function () {
        return this.as('h');
    };
    moment.duration.fn.asDays = function () {
        return this.as('d');
    };
    moment.duration.fn.asWeeks = function () {
        return this.as('weeks');
    };
    moment.duration.fn.asMonths = function () {
        return this.as('M');
    };
    moment.duration.fn.asYears = function () {
        return this.as('y');
    };

    /************************************
        Default Locale
    ************************************/


    // Set default locale, other locale will inherit from English.
    moment.locale('en', {
        ordinalParse: /\d{1,2}(th|st|nd|rd)/,
        ordinal : function (number) {
            var b = number % 10,
                output = (toInt(number % 100 / 10) === 1) ? 'th' :
                (b === 1) ? 'st' :
                (b === 2) ? 'nd' :
                (b === 3) ? 'rd' : 'th';
            return number + output;
        }
    });

    // moment.js locale configuration
// locale : afrikaans (af)
// author : Werner Mollentze : https://github.com/wernerm

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('af', {
        months : 'Januarie_Februarie_Maart_April_Mei_Junie_Julie_Augustus_September_Oktober_November_Desember'.split('_'),
        monthsShort : 'Jan_Feb_Mar_Apr_Mei_Jun_Jul_Aug_Sep_Okt_Nov_Des'.split('_'),
        weekdays : 'Sondag_Maandag_Dinsdag_Woensdag_Donderdag_Vrydag_Saterdag'.split('_'),
        weekdaysShort : 'Son_Maa_Din_Woe_Don_Vry_Sat'.split('_'),
        weekdaysMin : 'So_Ma_Di_Wo_Do_Vr_Sa'.split('_'),
        meridiemParse: /vm|nm/i,
        isPM : function (input) {
            return /^nm$/i.test(input);
        },
        meridiem : function (hours, minutes, isLower) {
            if (hours < 12) {
                return isLower ? 'vm' : 'VM';
            } else {
                return isLower ? 'nm' : 'NM';
            }
        },
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[Vandag om] LT',
            nextDay : '[M척re om] LT',
            nextWeek : 'dddd [om] LT',
            lastDay : '[Gister om] LT',
            lastWeek : '[Laas] dddd [om] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'oor %s',
            past : '%s gelede',
            s : '\'n paar sekondes',
            m : '\'n minuut',
            mm : '%d minute',
            h : '\'n uur',
            hh : '%d ure',
            d : '\'n dag',
            dd : '%d dae',
            M : '\'n maand',
            MM : '%d maande',
            y : '\'n jaar',
            yy : '%d jaar'
        },
        ordinalParse: /\d{1,2}(ste|de)/,
        ordinal : function (number) {
            return number + ((number === 1 || number === 8 || number >= 20) ? 'ste' : 'de'); // Thanks to Joris R철ling : https://github.com/jjupiter
        },
        week : {
            dow : 1, // Maandag is die eerste dag van die week.
            doy : 4  // Die week wat die 4de Januarie bevat is die eerste week van die jaar.
        }
    });
}));
// moment.js locale configuration
// locale : Moroccan Arabic (ar-ma)
// author : ElFadili Yassine : https://github.com/ElFadiliY
// author : Abdel Said : https://github.com/abdelsaid

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('ar-ma', {
        months : '��碼�邈_�磨邈碼�邈_�碼邈卍_粒磨邈��_�碼�_�����_�����万_曼娩魔_娩魔�磨邈_粒�魔�磨邈_���磨邈_膜寞�磨邈'.split('_'),
        monthsShort : '��碼�邈_�磨邈碼�邈_�碼邈卍_粒磨邈��_�碼�_�����_�����万_曼娩魔_娩魔�磨邈_粒�魔�磨邈_���磨邈_膜寞�磨邈'.split('_'),
        weekdays : '碼�粒幕膜_碼�瑪魔���_碼�麻�碼麻碼立_碼�粒邈磨晩碼立_碼�漠��卍_碼�寞�晩馬_碼�卍磨魔'.split('_'),
        weekdaysShort : '碼幕膜_碼魔���_麻�碼麻碼立_碼邈磨晩碼立_漠��卍_寞�晩馬_卍磨魔'.split('_'),
        weekdaysMin : '幕_�_麻_邈_漠_寞_卍'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[碼���� 晩�� 碼�卍碼晩馬] LT',
            nextDay: '[曼膜碼 晩�� 碼�卍碼晩馬] LT',
            nextWeek: 'dddd [晩�� 碼�卍碼晩馬] LT',
            lastDay: '[粒�卍 晩�� 碼�卍碼晩馬] LT',
            lastWeek: 'dddd [晩�� 碼�卍碼晩馬] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : '�� %s',
            past : '��莫 %s',
            s : '麻�碼�',
            m : '膜���馬',
            mm : '%d 膜�碼痲�',
            h : '卍碼晩馬',
            hh : '%d 卍碼晩碼魔',
            d : '���',
            dd : '%d 粒�碼�',
            M : '娩�邈',
            MM : '%d 粒娩�邈',
            y : '卍�馬',
            yy : '%d 卍��碼魔'
        },
        week : {
            dow : 6, // Saturday is the first day of the week.
            doy : 12  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Arabic Saudi Arabia (ar-sa)
// author : Suhail Alkowaileet : https://github.com/xsoh

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '蔑',
        '2': '冥',
        '3': '名',
        '4': '命',
        '5': '明',
        '6': '暝',
        '7': '椧',
        '8': '溟',
        '9': '皿',
        '0': '�'
    }, numberMap = {
        '蔑': '1',
        '冥': '2',
        '名': '3',
        '命': '4',
        '明': '5',
        '暝': '6',
        '椧': '7',
        '溟': '8',
        '皿': '9',
        '�': '0'
    };

    return moment.defineLocale('ar-sa', {
        months : '��碼�邈_�磨邈碼�邈_�碼邈卍_粒磨邈��_�碼��_�����_�����_粒曼卍慢卍_卍磨魔�磨邈_粒�魔�磨邈_����磨邈_膜�卍�磨邈'.split('_'),
        monthsShort : '��碼�邈_�磨邈碼�邈_�碼邈卍_粒磨邈��_�碼��_�����_�����_粒曼卍慢卍_卍磨魔�磨邈_粒�魔�磨邈_����磨邈_膜�卍�磨邈'.split('_'),
        weekdays : '碼�粒幕膜_碼�瑪麻���_碼�麻�碼麻碼立_碼�粒邈磨晩碼立_碼�漠��卍_碼�寞�晩馬_碼�卍磨魔'.split('_'),
        weekdaysShort : '粒幕膜_瑪麻���_麻�碼麻碼立_粒邈磨晩碼立_漠��卍_寞�晩馬_卍磨魔'.split('_'),
        weekdaysMin : '幕_�_麻_邈_漠_寞_卍'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'HH:mm:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        meridiemParse: /巒|�/,
        isPM : function (input) {
            return '�' === input;
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 12) {
                return '巒';
            } else {
                return '�';
            }
        },
        calendar : {
            sameDay: '[碼���� 晩�� 碼�卍碼晩馬] LT',
            nextDay: '[曼膜碼 晩�� 碼�卍碼晩馬] LT',
            nextWeek: 'dddd [晩�� 碼�卍碼晩馬] LT',
            lastDay: '[粒�卍 晩�� 碼�卍碼晩馬] LT',
            lastWeek: 'dddd [晩�� 碼�卍碼晩馬] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : '�� %s',
            past : '��莫 %s',
            s : '麻�碼�',
            m : '膜���馬',
            mm : '%d 膜�碼痲�',
            h : '卍碼晩馬',
            hh : '%d 卍碼晩碼魔',
            d : '���',
            dd : '%d 粒�碼�',
            M : '娩�邈',
            MM : '%d 粒娩�邈',
            y : '卍�馬',
            yy : '%d 卍��碼魔'
        },
        preparse: function (string) {
            return string.replace(/[蔑冥名命明暝椧溟皿�]/g, function (match) {
                return numberMap[match];
            }).replace(/�/g, ',');
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            }).replace(/,/g, '�');
        },
        week : {
            dow : 6, // Saturday is the first day of the week.
            doy : 12  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale  : Tunisian Arabic (ar-tn)

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('ar-tn', {
        months: '寞碼���_���邈�_�碼邈卍_粒�邈��_�碼�_寞�碼�_寞����馬_粒�魔_卍磨魔�磨邈_粒�魔�磨邈_����磨邈_膜�卍�磨邈'.split('_'),
        monthsShort: '寞碼���_���邈�_�碼邈卍_粒�邈��_�碼�_寞�碼�_寞����馬_粒�魔_卍磨魔�磨邈_粒�魔�磨邈_����磨邈_膜�卍�磨邈'.split('_'),
        weekdays: '碼�粒幕膜_碼�瑪麻���_碼�麻�碼麻碼立_碼�粒邈磨晩碼立_碼�漠��卍_碼�寞�晩馬_碼�卍磨魔'.split('_'),
        weekdaysShort: '粒幕膜_瑪麻���_麻�碼麻碼立_粒邈磨晩碼立_漠��卍_寞�晩馬_卍磨魔'.split('_'),
        weekdaysMin: '幕_�_麻_邈_漠_寞_卍'.split('_'),
        longDateFormat: {
            LT: 'HH:mm',
            LTS: 'LT:ss',
            L: 'DD/MM/YYYY',
            LL: 'D MMMM YYYY',
            LLL: 'D MMMM YYYY LT',
            LLLL: 'dddd D MMMM YYYY LT'
        },
        calendar: {
            sameDay: '[碼���� 晩�� 碼�卍碼晩馬] LT',
            nextDay: '[曼膜碼 晩�� 碼�卍碼晩馬] LT',
            nextWeek: 'dddd [晩�� 碼�卍碼晩馬] LT',
            lastDay: '[粒�卍 晩�� 碼�卍碼晩馬] LT',
            lastWeek: 'dddd [晩�� 碼�卍碼晩馬] LT',
            sameElse: 'L'
        },
        relativeTime: {
            future: '�� %s',
            past: '��莫 %s',
            s: '麻�碼�',
            m: '膜���馬',
            mm: '%d 膜�碼痲�',
            h: '卍碼晩馬',
            hh: '%d 卍碼晩碼魔',
            d: '���',
            dd: '%d 粒�碼�',
            M: '娩�邈',
            MM: '%d 粒娩�邈',
            y: '卍�馬',
            yy: '%d 卍��碼魔'
        },
        week: {
            dow: 1, // Monday is the first day of the week.
            doy: 4 // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// Locale: Arabic (ar)
// Author: Abdel Said: https://github.com/abdelsaid
// Changes in months, weekdays: Ahmed Elkhatib
// Native plural forms: forabi https://github.com/forabi

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '蔑',
        '2': '冥',
        '3': '名',
        '4': '命',
        '5': '明',
        '6': '暝',
        '7': '椧',
        '8': '溟',
        '9': '皿',
        '0': '�'
    }, numberMap = {
        '蔑': '1',
        '冥': '2',
        '名': '3',
        '命': '4',
        '明': '5',
        '暝': '6',
        '椧': '7',
        '溟': '8',
        '皿': '9',
        '�': '0'
    }, pluralForm = function (n) {
        return n === 0 ? 0 : n === 1 ? 1 : n === 2 ? 2 : n % 100 >= 3 && n % 100 <= 10 ? 3 : n % 100 >= 11 ? 4 : 5;
    }, plurals = {
        s : ['粒�� �� 麻碼��馬', '麻碼��馬 �碼幕膜馬', ['麻碼��魔碼�', '麻碼��魔��'], '%d 麻�碼�', '%d 麻碼��馬', '%d 麻碼��馬'],
        m : ['粒�� �� 膜���馬', '膜���馬 �碼幕膜馬', ['膜���魔碼�', '膜���魔��'], '%d 膜�碼痲�', '%d 膜���馬', '%d 膜���馬'],
        h : ['粒�� �� 卍碼晩馬', '卍碼晩馬 �碼幕膜馬', ['卍碼晩魔碼�', '卍碼晩魔��'], '%d 卍碼晩碼魔', '%d 卍碼晩馬', '%d 卍碼晩馬'],
        d : ['粒�� �� ���', '��� �碼幕膜', ['���碼�', '�����'], '%d 粒�碼�', '%d ����碼', '%d ���'],
        M : ['粒�� �� 娩�邈', '娩�邈 �碼幕膜', ['娩�邈碼�', '娩�邈��'], '%d 粒娩�邈', '%d 娩�邈碼', '%d 娩�邈'],
        y : ['粒�� �� 晩碼�', '晩碼� �碼幕膜', ['晩碼�碼�', '晩碼���'], '%d 粒晩�碼�', '%d 晩碼��碼', '%d 晩碼�']
    }, pluralize = function (u) {
        return function (number, withoutSuffix, string, isFuture) {
            var f = pluralForm(number),
                str = plurals[u][pluralForm(number)];
            if (f === 2) {
                str = str[withoutSuffix ? 0 : 1];
            }
            return str.replace(/%d/i, number);
        };
    }, months = [
        '�碼��� 碼�麻碼�� ��碼�邈',
        '娩磨碼慢 �磨邈碼�邈',
        '笠莫碼邈 �碼邈卍',
        '��卍碼� 粒磨邈��',
        '粒�碼邈 �碼��',
        '幕万�邈碼� �����',
        '魔��万 �����',
        '笠磨 粒曼卍慢卍',
        '粒���� 卍磨魔�磨邈',
        '魔娩邈�� 碼�粒�� 粒�魔�磨邈',
        '魔娩邈�� 碼�麻碼�� ����磨邈',
        '�碼��� 碼�粒�� 膜�卍�磨邈'
    ];

    return moment.defineLocale('ar', {
        months : months,
        monthsShort : months,
        weekdays : '碼�粒幕膜_碼�瑪麻���_碼�麻�碼麻碼立_碼�粒邈磨晩碼立_碼�漠��卍_碼�寞�晩馬_碼�卍磨魔'.split('_'),
        weekdaysShort : '粒幕膜_瑪麻���_麻�碼麻碼立_粒邈磨晩碼立_漠��卍_寞�晩馬_卍磨魔'.split('_'),
        weekdaysMin : '幕_�_麻_邈_漠_寞_卍'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'HH:mm:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        meridiemParse: /巒|�/,
        isPM : function (input) {
            return '�' === input;
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 12) {
                return '巒';
            } else {
                return '�';
            }
        },
        calendar : {
            sameDay: '[碼���� 晩�膜 碼�卍碼晩馬] LT',
            nextDay: '[曼膜�碼 晩�膜 碼�卍碼晩馬] LT',
            nextWeek: 'dddd [晩�膜 碼�卍碼晩馬] LT',
            lastDay: '[粒�卍 晩�膜 碼�卍碼晩馬] LT',
            lastWeek: 'dddd [晩�膜 碼�卍碼晩馬] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : '磨晩膜 %s',
            past : '��莫 %s',
            s : pluralize('s'),
            m : pluralize('m'),
            mm : pluralize('m'),
            h : pluralize('h'),
            hh : pluralize('h'),
            d : pluralize('d'),
            dd : pluralize('d'),
            M : pluralize('M'),
            MM : pluralize('M'),
            y : pluralize('y'),
            yy : pluralize('y')
        },
        preparse: function (string) {
            return string.replace(/[蔑冥名命明暝椧溟皿�]/g, function (match) {
                return numberMap[match];
            }).replace(/�/g, ',');
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            }).replace(/,/g, '�');
        },
        week : {
            dow : 6, // Saturday is the first day of the week.
            doy : 12  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : azerbaijani (az)
// author : topchiyev : https://github.com/topchiyev

(function (factory) {
    factory(moment);
}(function (moment) {
    var suffixes = {
        1: '-inci',
        5: '-inci',
        8: '-inci',
        70: '-inci',
        80: '-inci',

        2: '-nci',
        7: '-nci',
        20: '-nci',
        50: '-nci',

        3: '-체nc체',
        4: '-체nc체',
        100: '-체nc체',

        6: '-nc캇',

        9: '-uncu',
        10: '-uncu',
        30: '-uncu',

        60: '-캇nc캇',
        90: '-캇nc캇'
    };
    return moment.defineLocale('az', {
        months : 'yanvar_fevral_mart_aprel_may_iyun_iyul_avqust_sentyabr_oktyabr_noyabr_dekabr'.split('_'),
        monthsShort : 'yan_fev_mar_apr_may_iyn_iyl_avq_sen_okt_noy_dek'.split('_'),
        weekdays : 'Bazar_Bazar ert�si_횉�r힊�nb� ax힊am캇_횉�r힊�nb�_C체m� ax힊am캇_C체m�_힇�nb�'.split('_'),
        weekdaysShort : 'Baz_BzE_횉Ax_횉�r_CAx_C체m_힇�n'.split('_'),
        weekdaysMin : 'Bz_BE_횉A_횉�_CA_C체_힇�'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[bug체n saat] LT',
            nextDay : '[sabah saat] LT',
            nextWeek : '[g�l�n h�ft�] dddd [saat] LT',
            lastDay : '[d체n�n] LT',
            lastWeek : '[ke챌�n h�ft�] dddd [saat] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s sonra',
            past : '%s �vv�l',
            s : 'birne챌� saniyy�',
            m : 'bir d�qiq�',
            mm : '%d d�qiq�',
            h : 'bir saat',
            hh : '%d saat',
            d : 'bir g체n',
            dd : '%d g체n',
            M : 'bir ay',
            MM : '%d ay',
            y : 'bir il',
            yy : '%d il'
        },
        meridiemParse: /gec�|s�h�r|g체nd체z|ax힊am/,
        isPM : function (input) {
            return /^(g체nd체z|ax힊am)$/.test(input);
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 4) {
                return 'gec�';
            } else if (hour < 12) {
                return 's�h�r';
            } else if (hour < 17) {
                return 'g체nd체z';
            } else {
                return 'ax힊am';
            }
        },
        ordinalParse: /\d{1,2}-(캇nc캇|inci|nci|체nc체|nc캇|uncu)/,
        ordinal : function (number) {
            if (number === 0) {  // special case for zero
                return number + '-캇nc캇';
            }
            var a = number % 10,
                b = number % 100 - a,
                c = number >= 100 ? 100 : null;

            return number + (suffixes[a] || suffixes[b] || suffixes[c]);
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : belarusian (be)
// author : Dmitry Demidov : https://github.com/demidov91
// author: Praleska: http://praleska.pro/
// Author : Menelion Elens첬le : https://github.com/Oire

(function (factory) {
    factory(moment);
}(function (moment) {
    function plural(word, num) {
        var forms = word.split('_');
        return num % 10 === 1 && num % 100 !== 11 ? forms[0] : (num % 10 >= 2 && num % 10 <= 4 && (num % 100 < 10 || num % 100 >= 20) ? forms[1] : forms[2]);
    }

    function relativeTimeWithPlural(number, withoutSuffix, key) {
        var format = {
            'mm': withoutSuffix ? '�勻�剋�戟逵_�勻�剋�戟�_�勻�剋�戟' : '�勻�剋�戟�_�勻�剋�戟�_�勻�剋�戟',
            'hh': withoutSuffix ? '均逵畇鈞�戟逵_均逵畇鈞�戟�_均逵畇鈞�戟' : '均逵畇鈞�戟�_均逵畇鈞�戟�_均逵畇鈞�戟',
            'dd': '畇鈞筠戟�_畇戟�_畇鈞�戟',
            'MM': '劇筠���_劇筠����_劇筠���逵�',
            'yy': '均棘畇_均逵畇�_均逵畇棘�'
        };
        if (key === 'm') {
            return withoutSuffix ? '�勻�剋�戟逵' : '�勻�剋�戟�';
        }
        else if (key === 'h') {
            return withoutSuffix ? '均逵畇鈞�戟逵' : '均逵畇鈞�戟�';
        }
        else {
            return number + ' ' + plural(format[key], +number);
        }
    }

    function monthsCaseReplace(m, format) {
        var months = {
            'nominative': '���畇鈞筠戟�_剋���_�逵克逵勻�克_克�逵�逵勻�克_��逵勻筠戟�_���勻筠戟�_剋�極筠戟�_菌戟�勻筠戟�_勻筠�逵�筠戟�_克逵�����戟�克_剋���逵極逵畇_�戟筠菌逵戟�'.split('_'),
            'accusative': '���畇鈞筠戟�_剋��逵均逵_�逵克逵勻�克逵_克�逵�逵勻�克逵_��逵�戟�_���勻筠戟�_剋�極筠戟�_菌戟��戟�_勻筠�逵�戟�_克逵�����戟�克逵_剋���逵極逵畇逵_�戟筠菌戟�'.split('_')
        },

        nounCase = (/D[oD]?(\[[^\[\]]*\]|\s+)+MMMM?/).test(format) ?
            'accusative' :
            'nominative';

        return months[nounCase][m.month()];
    }

    function weekdaysCaseReplace(m, format) {
        var weekdays = {
            'nominative': '戟�畇鈞筠剋�_極逵戟�畇鈞筠剋逵克_逵��棘�逵克_�筠�逵畇逵_�逵�勻筠�_極��戟��逵_��閨棘�逵'.split('_'),
            'accusative': '戟�畇鈞筠剋�_極逵戟�畇鈞筠剋逵克_逵��棘�逵克_�筠�逵畇�_�逵�勻筠�_極��戟���_��閨棘��'.split('_')
        },

        nounCase = (/\[ ?[�勻] ?(?:劇�戟�剋��|戟逵���極戟��)? ?\] ?dddd/).test(format) ?
            'accusative' :
            'nominative';

        return weekdays[nounCase][m.day()];
    }

    return moment.defineLocale('be', {
        months : monthsCaseReplace,
        monthsShort : '���畇_剋��_�逵克_克�逵�_��逵勻_���勻_剋�極_菌戟�勻_勻筠�_克逵��_剋���_�戟筠菌'.split('_'),
        weekdays : weekdaysCaseReplace,
        weekdaysShort : '戟畇_極戟_逵�_��_��_極�_�閨'.split('_'),
        weekdaysMin : '戟畇_極戟_逵�_��_��_極�_�閨'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY 均.',
            LLL : 'D MMMM YYYY 均., LT',
            LLLL : 'dddd, D MMMM YYYY 均., LT'
        },
        calendar : {
            sameDay: '[鬼�戟戟� �] LT',
            nextDay: '[�逵���逵 �] LT',
            lastDay: '[叫�棘�逵 �] LT',
            nextWeek: function () {
                return '[叫] dddd [�] LT';
            },
            lastWeek: function () {
                switch (this.day()) {
                case 0:
                case 3:
                case 5:
                case 6:
                    return '[叫 劇�戟�剋��] dddd [�] LT';
                case 1:
                case 2:
                case 4:
                    return '[叫 劇�戟�剋�] dddd [�] LT';
                }
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : '極�逵鈞 %s',
            past : '%s �逵劇�',
            s : '戟筠克逵剋�克� �筠克�戟畇',
            m : relativeTimeWithPlural,
            mm : relativeTimeWithPlural,
            h : relativeTimeWithPlural,
            hh : relativeTimeWithPlural,
            d : '畇鈞筠戟�',
            dd : relativeTimeWithPlural,
            M : '劇筠���',
            MM : relativeTimeWithPlural,
            y : '均棘畇',
            yy : relativeTimeWithPlural
        },
        meridiemParse: /戟棘��|�逵戟���|畇戟�|勻筠�逵�逵/,
        isPM : function (input) {
            return /^(畇戟�|勻筠�逵�逵)$/.test(input);
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 4) {
                return '戟棘��';
            } else if (hour < 12) {
                return '�逵戟���';
            } else if (hour < 17) {
                return '畇戟�';
            } else {
                return '勻筠�逵�逵';
            }
        },

        ordinalParse: /\d{1,2}-(�|�|均逵)/,
        ordinal: function (number, period) {
            switch (period) {
            case 'M':
            case 'd':
            case 'DDD':
            case 'w':
            case 'W':
                return (number % 10 === 2 || number % 10 === 3) && (number % 100 !== 12 && number % 100 !== 13) ? number + '-�' : number + '-�';
            case 'D':
                return number + '-均逵';
            default:
                return number;
            }
        },

        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : bulgarian (bg)
// author : Krasen Borisov : https://github.com/kraz

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('bg', {
        months : '�戟�逵�龜_�筠勻��逵�龜_劇逵��_逵極�龜剋_劇逵橘_�戟龜_�剋龜_逵勻均���_�筠極�筠劇勻�龜_棘克�棘劇勻�龜_戟棘筠劇勻�龜_畇筠克筠劇勻�龜'.split('_'),
        monthsShort : '�戟�_�筠勻_劇逵�_逵極�_劇逵橘_�戟龜_�剋龜_逵勻均_�筠極_棘克�_戟棘筠_畇筠克'.split('_'),
        weekdays : '戟筠畇筠剋�_極棘戟筠畇筠剋戟龜克_勻�棘�戟龜克_���畇逵_�筠�勻����克_極筠��克_��閨棘�逵'.split('_'),
        weekdaysShort : '戟筠畇_極棘戟_勻�棘_���_�筠�_極筠�_��閨'.split('_'),
        weekdaysMin : '戟畇_極戟_勻�_��_��_極�_�閨'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'D.MM.YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[�戟筠� 勻] LT',
            nextDay : '[叫��筠 勻] LT',
            nextWeek : 'dddd [勻] LT',
            lastDay : '[��筠�逵 勻] LT',
            lastWeek : function () {
                switch (this.day()) {
                case 0:
                case 3:
                case 6:
                    return '[� 龜鈞劇龜戟逵剋逵�逵] dddd [勻] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[� 龜鈞劇龜戟逵剋龜�] dddd [勻] LT';
                }
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : '�剋筠畇 %s',
            past : '極�筠畇龜 %s',
            s : '戟�克棘剋克棘 �筠克�戟畇龜',
            m : '劇龜戟��逵',
            mm : '%d 劇龜戟��龜',
            h : '�逵�',
            hh : '%d �逵�逵',
            d : '畇筠戟',
            dd : '%d 畇戟龜',
            M : '劇筠�筠�',
            MM : '%d 劇筠�筠�逵',
            y : '均棘畇龜戟逵',
            yy : '%d 均棘畇龜戟龜'
        },
        ordinalParse: /\d{1,2}-(筠勻|筠戟|�龜|勻龜|�龜|劇龜)/,
        ordinal : function (number) {
            var lastDigit = number % 10,
                last2Digits = number % 100;
            if (number === 0) {
                return number + '-筠勻';
            } else if (last2Digits === 0) {
                return number + '-筠戟';
            } else if (last2Digits > 10 && last2Digits < 20) {
                return number + '-�龜';
            } else if (lastDigit === 1) {
                return number + '-勻龜';
            } else if (lastDigit === 2) {
                return number + '-�龜';
            } else if (lastDigit === 7 || lastDigit === 8) {
                return number + '-劇龜';
            } else {
                return number + '-�龜';
            }
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Bengali (bn)
// author : Kaushik Gandhi : https://github.com/kaushikgandhi

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '鋤�',
        '2': '鋤�',
        '3': '鋤�',
        '4': '鋤�',
        '5': '鋤�',
        '6': '鋤�',
        '7': '鋤�',
        '8': '鋤�',
        '9': '鋤�',
        '0': '鋤�'
    },
    numberMap = {
        '鋤�': '1',
        '鋤�': '2',
        '鋤�': '3',
        '鋤�': '4',
        '鋤�': '5',
        '鋤�': '6',
        '鋤�': '7',
        '鋤�': '8',
        '鋤�': '9',
        '鋤�': '0'
    };

    return moment.defineLocale('bn', {
        months : '逝쒉┥逝ⓣ쭅鋤잀┥逝겯�_逝ム쭎逝о쭅鋤잀┥逝겯�_逝�┥逝겯쭕逝�_逝뤲┴鋤띭┛逝욈┣_逝�쭎_逝쒉쭅逝�_逝쒉쭅逝꿋┥逝�_逝끶쬀逝약┯鋤띭쬉_逝멘쭎逝む쭕逝잀쭎逝�쭕逝о┛_逝끶쫾鋤띭쬉鋤뗠━逝�_逝ⓣ┃鋤뉋┏鋤띭━逝�_逝□┸逝멘쭎逝�쭕逝о┛'.split('_'),
        monthsShort : '逝쒉┥逝ⓣ쭅_逝ム쭎逝�_逝�┥逝겯쭕逝�_逝뤲┴逝�_逝�쭎_逝쒉쭅逝�_逝쒉쭅逝�_逝끶쬀_逝멘쭎逝む쭕逝�_逝끶쫾鋤띭쬉鋤�_逝ⓣ┃_逝□┸逝멘쭎逝�쭕'.split('_'),
        weekdays : '逝겯━逝욈━逝약┛_逝멘쭓逝�━逝약┛_逝�쬂鋤띭쬀逝꿋━逝약┛_逝о쭅逝㏅━逝약┛_逝о쭇逝밝┯鋤띭┴逝ㅰ쭕逝ㅰ┸逝о┥逝�_逝뜩쭅逝뺖쭕逝겯쭅逝о┥逝�_逝뜩┬逝욈━逝약┛'.split('_'),
        weekdaysShort : '逝겯━逝�_逝멘쭓逝�_逝�쬂鋤띭쬀逝�_逝о쭅逝�_逝о쭇逝밝┯鋤띭┴逝ㅰ쭕逝ㅰ┸_逝뜩쭅逝뺖쭕逝겯쭅_逝뜩┬逝�'.split('_'),
        weekdaysMin : '逝겯━_逝멘┏_逝�쬂鋤띭쬀_逝о쭅_逝о쭕逝겯┸逝�_逝뜩쭅_逝뜩┬逝�'.split('_'),
        longDateFormat : {
            LT : 'A h:mm 逝멘┏鋤�',
            LTS : 'A h:mm:ss 逝멘┏鋤�',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY, LT',
            LLLL : 'dddd, D MMMM YYYY, LT'
        },
        calendar : {
            sameDay : '[逝녱쬅] LT',
            nextDay : '[逝녱쬀逝약┏鋤逝뺖┥逝�] LT',
            nextWeek : 'dddd, LT',
            lastDay : '[逝쀠┐逝뺖┥逝�] LT',
            lastWeek : '[逝쀠┐] dddd, LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s 逝む┛鋤�',
            past : '%s 逝녱쬀鋤�',
            s : '逝뺖쫸逝� 逝멘쭎逝뺖쭎逝ⓣ쭕逝�',
            m : '逝뤲쫾 逝�┸逝ⓣ┸逝�',
            mm : '%d 逝�┸逝ⓣ┸逝�',
            h : '逝뤲쫾 逝섁┬鋤띭쬉逝�',
            hh : '%d 逝섁┬鋤띭쬉逝�',
            d : '逝뤲쫾 逝╆┸逝�',
            dd : '%d 逝╆┸逝�',
            M : '逝뤲쫾 逝�┥逝�',
            MM : '%d 逝�┥逝�',
            y : '逝뤲쫾 逝о쬄逝�',
            yy : '%d 逝о쬄逝�'
        },
        preparse: function (string) {
            return string.replace(/[鋤㏅㎤鋤⒯㎦鋤ム㎚鋤�㎜鋤�㏄]/g, function (match) {
                return numberMap[match];
            });
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            });
        },
        meridiemParse: /逝겯┥逝�|逝뜩쫾逝약┣|逝╆쭅逝む쭅逝�|逝о┸逝뺖쭎逝�|逝겯┥逝�/,
        isPM: function (input) {
            return /^(逝╆쭅逝む쭅逝�|逝о┸逝뺖쭎逝�|逝겯┥逝�)$/.test(input);
        },
        //Bengali is a vast language its spoken
        //in different forms in various parts of the world.
        //I have just generalized with most common one used
        meridiem : function (hour, minute, isLower) {
            if (hour < 4) {
                return '逝겯┥逝�';
            } else if (hour < 10) {
                return '逝뜩쫾逝약┣';
            } else if (hour < 17) {
                return '逝╆쭅逝む쭅逝�';
            } else if (hour < 20) {
                return '逝о┸逝뺖쭎逝�';
            } else {
                return '逝겯┥逝�';
            }
        },
        week : {
            dow : 0, // Sunday is the first day of the week.
            doy : 6  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : tibetan (bo)
// author : Thupten N. Chakrishar : https://github.com/vajradog

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '善�',
        '2': '善�',
        '3': '善�',
        '4': '善�',
        '5': '善�',
        '6': '善�',
        '7': '善�',
        '8': '善�',
        '9': '善�',
        '0': '善�'
    },
    numberMap = {
        '善�': '1',
        '善�': '2',
        '善�': '3',
        '善�': '4',
        '善�': '5',
        '善�': '6',
        '善�': '7',
        '善�': '8',
        '善�': '9',
        '善�': '0'
    };

    return moment.defineLocale('bo', {
        months : '嬋잀쓱善뗠퐭善뗠퐨嬋꾝펻嬋붲슨_嬋잀쓱善뗠퐭善뗠퐘嬋됢쉿嬋╆펻嬋�_嬋잀쓱善뗠퐭善뗠퐘嬋╆슈嬋섁펻嬋�_嬋잀쓱善뗠퐭善뗠퐭嬋왽쉿善뗠퐫_嬋잀쓱善뗠퐭善뗠숲宣붲펻嬋�_嬋잀쓱善뗠퐭善뗠퐨宣꿋슈嬋귖펻嬋�_嬋잀쓱善뗠퐭善뗠퐭嬋묂슈嬋볙펻嬋�_嬋잀쓱善뗠퐭善뗠퐭嬋№풎宣긍퐨善뗠퐫_嬋잀쓱善뗠퐭善뗠퐨嬋귖슈善뗠퐫_嬋잀쓱善뗠퐭善뗠퐭嬋끶슈善뗠퐫_嬋잀쓱善뗠퐭善뗠퐭嬋끶슈善뗠퐘嬋끶쉿嬋귖펻嬋�_嬋잀쓱善뗠퐭善뗠퐭嬋끶슈善뗠퐘嬋됢쉿嬋╆펻嬋�'.split('_'),
        monthsShort : '嬋잀쓱善뗠퐭善뗠퐨嬋꾝펻嬋붲슨_嬋잀쓱善뗠퐭善뗠퐘嬋됢쉿嬋╆펻嬋�_嬋잀쓱善뗠퐭善뗠퐘嬋╆슈嬋섁펻嬋�_嬋잀쓱善뗠퐭善뗠퐭嬋왽쉿善뗠퐫_嬋잀쓱善뗠퐭善뗠숲宣붲펻嬋�_嬋잀쓱善뗠퐭善뗠퐨宣꿋슈嬋귖펻嬋�_嬋잀쓱善뗠퐭善뗠퐭嬋묂슈嬋볙펻嬋�_嬋잀쓱善뗠퐭善뗠퐭嬋№풎宣긍퐨善뗠퐫_嬋잀쓱善뗠퐭善뗠퐨嬋귖슈善뗠퐫_嬋잀쓱善뗠퐭善뗠퐭嬋끶슈善뗠퐫_嬋잀쓱善뗠퐭善뗠퐭嬋끶슈善뗠퐘嬋끶쉿嬋귖펻嬋�_嬋잀쓱善뗠퐭善뗠퐭嬋끶슈善뗠퐘嬋됢쉿嬋╆펻嬋�'.split('_'),
        weekdays : '嬋귖퐶嬋졷펻嬋됢쉿善뗠퐯善�_嬋귖퐶嬋졷펻嬋잀쓱善뗠퐭善�_嬋귖퐶嬋졷펻嬋섁쉿嬋귖펻嬋묂퐯嬋№펻_嬋귖퐶嬋졷펻嬋｀쓿嬋귖펻嬋붲펻_嬋귖퐶嬋졷펻嬋뺖슈嬋№펻嬋뽤슈_嬋귖퐶嬋졷펻嬋붲펻嬋╆퐚嬋╆펻_嬋귖퐶嬋졷펻嬋╆쑈嬋뷕퐪善뗠퐫善�'.split('_'),
        weekdaysShort : '嬋됢쉿善뗠퐯善�_嬋잀쓱善뗠퐭善�_嬋섁쉿嬋귖펻嬋묂퐯嬋№펻_嬋｀쓿嬋귖펻嬋붲펻_嬋뺖슈嬋№펻嬋뽤슈_嬋붲펻嬋╆퐚嬋╆펻_嬋╆쑈嬋뷕퐪善뗠퐫善�'.split('_'),
        weekdaysMin : '嬋됢쉿善뗠퐯善�_嬋잀쓱善뗠퐭善�_嬋섁쉿嬋귖펻嬋묂퐯嬋№펻_嬋｀쓿嬋귖펻嬋붲펻_嬋뺖슈嬋№펻嬋뽤슈_嬋붲펻嬋╆퐚嬋╆펻_嬋╆쑈嬋뷕퐪善뗠퐫善�'.split('_'),
        longDateFormat : {
            LT : 'A h:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY, LT',
            LLLL : 'dddd, D MMMM YYYY, LT'
        },
        calendar : {
            sameDay : '[嬋묂쉿善뗠숱嬋꿋퐚] LT',
            nextDay : '[嬋╆퐚善뗠퐠嬋꿋퐪] LT',
            nextWeek : '[嬋뽤퐨嬋닮퐪善뗠퐬宣꿋퐘善뗠숱宣쀠스嬋╆펻嬋�], LT',
            lastDay : '[嬋곟펻嬋╆퐚] LT',
            lastWeek : '[嬋뽤퐨嬋닮퐪善뗠퐬宣꿋퐘善뗠퐯嬋먣퐷善뗠퐯] dddd, LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s 嬋｀펻',
            past : '%s 嬋╆풐嬋볙펻嬋�',
            s : '嬋｀퐯善뗠쉐嬋�',
            m : '嬋╆풊嬋№펻嬋섁펻嬋귖퐛嬋꿋퐘',
            mm : '%d 嬋╆풊嬋№펻嬋�',
            h : '嬋녱슈善뗠퐱嬋솰퐨善뗠퐘嬋끶쉿嬋�',
            hh : '%d 嬋녱슈善뗠퐱嬋솰퐨',
            d : '嬋됢쉿嬋볙펻嬋귖퐛嬋꿋퐘',
            dd : '%d 嬋됢쉿嬋볙펻',
            M : '嬋잀쓱善뗠퐭善뗠퐘嬋끶쉿嬋�',
            MM : '%d 嬋잀쓱善뗠퐭',
            y : '嬋｀슨善뗠퐘嬋끶쉿嬋�',
            yy : '%d 嬋｀슨'
        },
        preparse: function (string) {
            return string.replace(/[善□샨善｀샴善�샷善㏅섀善⒯폖]/g, function (match) {
                return numberMap[match];
            });
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            });
        },
        meridiemParse: /嬋섁퐱嬋볙펻嬋섁슨|嬋왽슨嬋귖쉐善뗠�嬋�|嬋됢쉿嬋볙펻嬋귖슈嬋�|嬋묂퐘嬋솰퐚善뗠퐨嬋�|嬋섁퐱嬋볙펻嬋섁슨/,
        isPM: function (input) {
            return /^(嬋됢쉿嬋볙펻嬋귖슈嬋�|嬋묂퐘嬋솰퐚善뗠퐨嬋�|嬋섁퐱嬋볙펻嬋섁슨)$/.test(input);
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 4) {
                return '嬋섁퐱嬋볙펻嬋섁슨';
            } else if (hour < 10) {
                return '嬋왽슨嬋귖쉐善뗠�嬋�';
            } else if (hour < 17) {
                return '嬋됢쉿嬋볙펻嬋귖슈嬋�';
            } else if (hour < 20) {
                return '嬋묂퐘嬋솰퐚善뗠퐨嬋�';
            } else {
                return '嬋섁퐱嬋볙펻嬋섁슨';
            }
        },
        week : {
            dow : 0, // Sunday is the first day of the week.
            doy : 6  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : breton (br)
// author : Jean-Baptiste Le Duigou : https://github.com/jbleduigou

(function (factory) {
    factory(moment);
}(function (moment) {
    function relativeTimeWithMutation(number, withoutSuffix, key) {
        var format = {
            'mm': 'munutenn',
            'MM': 'miz',
            'dd': 'devezh'
        };
        return number + ' ' + mutation(format[key], number);
    }

    function specialMutationForYears(number) {
        switch (lastNumber(number)) {
        case 1:
        case 3:
        case 4:
        case 5:
        case 9:
            return number + ' bloaz';
        default:
            return number + ' vloaz';
        }
    }

    function lastNumber(number) {
        if (number > 9) {
            return lastNumber(number % 10);
        }
        return number;
    }

    function mutation(text, number) {
        if (number === 2) {
            return softMutation(text);
        }
        return text;
    }

    function softMutation(text) {
        var mutationTable = {
            'm': 'v',
            'b': 'v',
            'd': 'z'
        };
        if (mutationTable[text.charAt(0)] === undefined) {
            return text;
        }
        return mutationTable[text.charAt(0)] + text.substring(1);
    }

    return moment.defineLocale('br', {
        months : 'Genver_C\'hwevrer_Meurzh_Ebrel_Mae_Mezheven_Gouere_Eost_Gwengolo_Here_Du_Kerzu'.split('_'),
        monthsShort : 'Gen_C\'hwe_Meu_Ebr_Mae_Eve_Gou_Eos_Gwe_Her_Du_Ker'.split('_'),
        weekdays : 'Sul_Lun_Meurzh_Merc\'her_Yaou_Gwener_Sadorn'.split('_'),
        weekdaysShort : 'Sul_Lun_Meu_Mer_Yao_Gwe_Sad'.split('_'),
        weekdaysMin : 'Su_Lu_Me_Mer_Ya_Gw_Sa'.split('_'),
        longDateFormat : {
            LT : 'h[e]mm A',
            LTS : 'h[e]mm:ss A',
            L : 'DD/MM/YYYY',
            LL : 'D [a viz] MMMM YYYY',
            LLL : 'D [a viz] MMMM YYYY LT',
            LLLL : 'dddd, D [a viz] MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[Hiziv da] LT',
            nextDay : '[Warc\'hoazh da] LT',
            nextWeek : 'dddd [da] LT',
            lastDay : '[Dec\'h da] LT',
            lastWeek : 'dddd [paset da] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'a-benn %s',
            past : '%s \'zo',
            s : 'un nebeud segondenno첫',
            m : 'ur vunutenn',
            mm : relativeTimeWithMutation,
            h : 'un eur',
            hh : '%d eur',
            d : 'un devezh',
            dd : relativeTimeWithMutation,
            M : 'ur miz',
            MM : relativeTimeWithMutation,
            y : 'ur bloaz',
            yy : specialMutationForYears
        },
        ordinalParse: /\d{1,2}(a챰|vet)/,
        ordinal : function (number) {
            var output = (number === 1) ? 'a챰' : 'vet';
            return number + output;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : bosnian (bs)
// author : Nedim Cholich : https://github.com/frontyard
// based on (hr) translation by Bojan Markovi훶

(function (factory) {
    factory(moment);
}(function (moment) {
    function translate(number, withoutSuffix, key) {
        var result = number + ' ';
        switch (key) {
        case 'm':
            return withoutSuffix ? 'jedna minuta' : 'jedne minute';
        case 'mm':
            if (number === 1) {
                result += 'minuta';
            } else if (number === 2 || number === 3 || number === 4) {
                result += 'minute';
            } else {
                result += 'minuta';
            }
            return result;
        case 'h':
            return withoutSuffix ? 'jedan sat' : 'jednog sata';
        case 'hh':
            if (number === 1) {
                result += 'sat';
            } else if (number === 2 || number === 3 || number === 4) {
                result += 'sata';
            } else {
                result += 'sati';
            }
            return result;
        case 'dd':
            if (number === 1) {
                result += 'dan';
            } else {
                result += 'dana';
            }
            return result;
        case 'MM':
            if (number === 1) {
                result += 'mjesec';
            } else if (number === 2 || number === 3 || number === 4) {
                result += 'mjeseca';
            } else {
                result += 'mjeseci';
            }
            return result;
        case 'yy':
            if (number === 1) {
                result += 'godina';
            } else if (number === 2 || number === 3 || number === 4) {
                result += 'godine';
            } else {
                result += 'godina';
            }
            return result;
        }
    }

    return moment.defineLocale('bs', {
        months : 'januar_februar_mart_april_maj_juni_juli_august_septembar_oktobar_novembar_decembar'.split('_'),
        monthsShort : 'jan._feb._mar._apr._maj._jun._jul._aug._sep._okt._nov._dec.'.split('_'),
        weekdays : 'nedjelja_ponedjeljak_utorak_srijeda_훾etvrtak_petak_subota'.split('_'),
        weekdaysShort : 'ned._pon._uto._sri._훾et._pet._sub.'.split('_'),
        weekdaysMin : 'ne_po_ut_sr_훾e_pe_su'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'DD. MM. YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY LT',
            LLLL : 'dddd, D. MMMM YYYY LT'
        },
        calendar : {
            sameDay  : '[danas u] LT',
            nextDay  : '[sutra u] LT',

            nextWeek : function () {
                switch (this.day()) {
                case 0:
                    return '[u] [nedjelju] [u] LT';
                case 3:
                    return '[u] [srijedu] [u] LT';
                case 6:
                    return '[u] [subotu] [u] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[u] dddd [u] LT';
                }
            },
            lastDay  : '[ju훾er u] LT',
            lastWeek : function () {
                switch (this.day()) {
                case 0:
                case 3:
                    return '[pro큄lu] dddd [u] LT';
                case 6:
                    return '[pro큄le] [subote] [u] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[pro큄li] dddd [u] LT';
                }
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : 'za %s',
            past   : 'prije %s',
            s      : 'par sekundi',
            m      : translate,
            mm     : translate,
            h      : translate,
            hh     : translate,
            d      : 'dan',
            dd     : translate,
            M      : 'mjesec',
            MM     : translate,
            y      : 'godinu',
            yy     : translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : catalan (ca)
// author : Juan G. Hurtado : https://github.com/juanghurtado

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('ca', {
        months : 'gener_febrer_mar챌_abril_maig_juny_juliol_agost_setembre_octubre_novembre_desembre'.split('_'),
        monthsShort : 'gen._febr._mar._abr._mai._jun._jul._ag._set._oct._nov._des.'.split('_'),
        weekdays : 'diumenge_dilluns_dimarts_dimecres_dijous_divendres_dissabte'.split('_'),
        weekdaysShort : 'dg._dl._dt._dc._dj._dv._ds.'.split('_'),
        weekdaysMin : 'Dg_Dl_Dt_Dc_Dj_Dv_Ds'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay : function () {
                return '[avui a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
            },
            nextDay : function () {
                return '[dem횪 a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
            },
            nextWeek : function () {
                return 'dddd [a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
            },
            lastDay : function () {
                return '[ahir a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
            },
            lastWeek : function () {
                return '[el] dddd [passat a ' + ((this.hours() !== 1) ? 'les' : 'la') + '] LT';
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : 'en %s',
            past : 'fa %s',
            s : 'uns segons',
            m : 'un minut',
            mm : '%d minuts',
            h : 'una hora',
            hh : '%d hores',
            d : 'un dia',
            dd : '%d dies',
            M : 'un mes',
            MM : '%d mesos',
            y : 'un any',
            yy : '%d anys'
        },
        ordinalParse: /\d{1,2}(r|n|t|챔|a)/,
        ordinal : function (number, period) {
            var output = (number === 1) ? 'r' :
                (number === 2) ? 'n' :
                (number === 3) ? 'r' :
                (number === 4) ? 't' : '챔';
            if (period === 'w' || period === 'W') {
                output = 'a';
            }
            return number + output;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : czech (cs)
// author : petrbela : https://github.com/petrbela

(function (factory) {
    factory(moment);
}(function (moment) {
    var months = 'leden_첬nor_b힂ezen_duben_kv휎ten_훾erven_훾ervenec_srpen_z찼힂챠_힂챠jen_listopad_prosinec'.split('_'),
        monthsShort = 'led_첬no_b힂e_dub_kv휎_훾vn_훾vc_srp_z찼힂_힂챠j_lis_pro'.split('_');

    function plural(n) {
        return (n > 1) && (n < 5) && (~~(n / 10) !== 1);
    }

    function translate(number, withoutSuffix, key, isFuture) {
        var result = number + ' ';
        switch (key) {
        case 's':  // a few seconds / in a few seconds / a few seconds ago
            return (withoutSuffix || isFuture) ? 'p찼r sekund' : 'p찼r sekundami';
        case 'm':  // a minute / in a minute / a minute ago
            return withoutSuffix ? 'minuta' : (isFuture ? 'minutu' : 'minutou');
        case 'mm': // 9 minutes / in 9 minutes / 9 minutes ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'minuty' : 'minut');
            } else {
                return result + 'minutami';
            }
            break;
        case 'h':  // an hour / in an hour / an hour ago
            return withoutSuffix ? 'hodina' : (isFuture ? 'hodinu' : 'hodinou');
        case 'hh': // 9 hours / in 9 hours / 9 hours ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'hodiny' : 'hodin');
            } else {
                return result + 'hodinami';
            }
            break;
        case 'd':  // a day / in a day / a day ago
            return (withoutSuffix || isFuture) ? 'den' : 'dnem';
        case 'dd': // 9 days / in 9 days / 9 days ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'dny' : 'dn챠');
            } else {
                return result + 'dny';
            }
            break;
        case 'M':  // a month / in a month / a month ago
            return (withoutSuffix || isFuture) ? 'm휎s챠c' : 'm휎s챠cem';
        case 'MM': // 9 months / in 9 months / 9 months ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'm휎s챠ce' : 'm휎s챠c킁');
            } else {
                return result + 'm휎s챠ci';
            }
            break;
        case 'y':  // a year / in a year / a year ago
            return (withoutSuffix || isFuture) ? 'rok' : 'rokem';
        case 'yy': // 9 years / in 9 years / 9 years ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'roky' : 'let');
            } else {
                return result + 'lety';
            }
            break;
        }
    }

    return moment.defineLocale('cs', {
        months : months,
        monthsShort : monthsShort,
        monthsParse : (function (months, monthsShort) {
            var i, _monthsParse = [];
            for (i = 0; i < 12; i++) {
                // use custom parser to solve problem with July (훾ervenec)
                _monthsParse[i] = new RegExp('^' + months[i] + '$|^' + monthsShort[i] + '$', 'i');
            }
            return _monthsParse;
        }(months, monthsShort)),
        weekdays : 'ned휎le_pond휎l챠_첬ter첵_st힂eda_훾tvrtek_p찼tek_sobota'.split('_'),
        weekdaysShort : 'ne_po_첬t_st_훾t_p찼_so'.split('_'),
        weekdaysMin : 'ne_po_첬t_st_훾t_p찼_so'.split('_'),
        longDateFormat : {
            LT: 'H:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY LT',
            LLLL : 'dddd D. MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[dnes v] LT',
            nextDay: '[z챠tra v] LT',
            nextWeek: function () {
                switch (this.day()) {
                case 0:
                    return '[v ned휎li v] LT';
                case 1:
                case 2:
                    return '[v] dddd [v] LT';
                case 3:
                    return '[ve st힂edu v] LT';
                case 4:
                    return '[ve 훾tvrtek v] LT';
                case 5:
                    return '[v p찼tek v] LT';
                case 6:
                    return '[v sobotu v] LT';
                }
            },
            lastDay: '[v훾era v] LT',
            lastWeek: function () {
                switch (this.day()) {
                case 0:
                    return '[minulou ned휎li v] LT';
                case 1:
                case 2:
                    return '[minul챕] dddd [v] LT';
                case 3:
                    return '[minulou st힂edu v] LT';
                case 4:
                case 5:
                    return '[minul첵] dddd [v] LT';
                case 6:
                    return '[minulou sobotu v] LT';
                }
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : 'za %s',
            past : 'p힂ed %s',
            s : translate,
            m : translate,
            mm : translate,
            h : translate,
            hh : translate,
            d : translate,
            dd : translate,
            M : translate,
            MM : translate,
            y : translate,
            yy : translate
        },
        ordinalParse : /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : chuvash (cv)
// author : Anatoly Mironov : https://github.com/mirontoli

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('cv', {
        months : '克훱�剋逵�_戟逵�훱�_極��_逵克逵_劇逵橘_챌휈��劇筠_��훱_챌��剋逵_逵勻훱戟_�極逵_�鄲克_�逵��逵勻'.split('_'),
        monthsShort : '克훱�_戟逵�_極��_逵克逵_劇逵橘_챌휈�_��훱_챌��_逵勻_�極逵_�鄲克_�逵�'.split('_'),
        weekdays : '勻���逵�戟龜克�戟_��戟�龜克�戟_��剋逵�龜克�戟_�戟克�戟_克휈챌戟筠�戟龜克�戟_��戟筠克�戟_�훱劇逵�克�戟'.split('_'),
        weekdaysShort : '勻��_��戟_��剋_�戟_克휈챌_��戟_�훱劇'.split('_'),
        weekdaysMin : '勻�_�戟_��_�戟_克챌_��_�劇'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD-MM-YYYY',
            LL : 'YYYY [챌�剋�龜] MMMM [�橘훱�휈戟] D[-劇휈�휈]',
            LLL : 'YYYY [챌�剋�龜] MMMM [�橘훱�휈戟] D[-劇휈�휈], LT',
            LLLL : 'dddd, YYYY [챌�剋�龜] MMMM [�橘훱�휈戟] D[-劇휈�휈], LT'
        },
        calendar : {
            sameDay: '[�逵�戟] LT [�筠�筠��筠]',
            nextDay: '[竅�逵戟] LT [�筠�筠��筠]',
            lastDay: '[휇戟筠�] LT [�筠�筠��筠]',
            nextWeek: '[횉龜�筠�] dddd LT [�筠�筠��筠]',
            lastWeek: '[���戟휈] dddd LT [�筠�筠��筠]',
            sameElse: 'L'
        },
        relativeTime : {
            future : function (output) {
                var affix = /�筠�筠�$/i.exec(output) ? '�筠戟' : /챌�剋$/i.exec(output) ? '�逵戟' : '�逵戟';
                return output + affix;
            },
            past : '%s 克逵�剋剋逵',
            s : '極휈�-龜克 챌筠克克�戟�',
            m : '極휈� 劇龜戟��',
            mm : '%d 劇龜戟��',
            h : '極휈� �筠�筠�',
            hh : '%d �筠�筠�',
            d : '極휈� 克�戟',
            dd : '%d 克�戟',
            M : '極휈� �橘훱�',
            MM : '%d �橘훱�',
            y : '極휈� 챌�剋',
            yy : '%d 챌�剋'
        },
        ordinalParse: /\d{1,2}-劇휈�/,
        ordinal : '%d-劇휈�',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Welsh (cy)
// author : Robert Allen

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('cy', {
        months: 'Ionawr_Chwefror_Mawrth_Ebrill_Mai_Mehefin_Gorffennaf_Awst_Medi_Hydref_Tachwedd_Rhagfyr'.split('_'),
        monthsShort: 'Ion_Chwe_Maw_Ebr_Mai_Meh_Gor_Aws_Med_Hyd_Tach_Rhag'.split('_'),
        weekdays: 'Dydd Sul_Dydd Llun_Dydd Mawrth_Dydd Mercher_Dydd Iau_Dydd Gwener_Dydd Sadwrn'.split('_'),
        weekdaysShort: 'Sul_Llun_Maw_Mer_Iau_Gwe_Sad'.split('_'),
        weekdaysMin: 'Su_Ll_Ma_Me_Ia_Gw_Sa'.split('_'),
        // time formats are the same as en-gb
        longDateFormat: {
            LT: 'HH:mm',
            LTS : 'LT:ss',
            L: 'DD/MM/YYYY',
            LL: 'D MMMM YYYY',
            LLL: 'D MMMM YYYY LT',
            LLLL: 'dddd, D MMMM YYYY LT'
        },
        calendar: {
            sameDay: '[Heddiw am] LT',
            nextDay: '[Yfory am] LT',
            nextWeek: 'dddd [am] LT',
            lastDay: '[Ddoe am] LT',
            lastWeek: 'dddd [diwethaf am] LT',
            sameElse: 'L'
        },
        relativeTime: {
            future: 'mewn %s',
            past: '%s yn 척l',
            s: 'ychydig eiliadau',
            m: 'munud',
            mm: '%d munud',
            h: 'awr',
            hh: '%d awr',
            d: 'diwrnod',
            dd: '%d diwrnod',
            M: 'mis',
            MM: '%d mis',
            y: 'blwyddyn',
            yy: '%d flynedd'
        },
        ordinalParse: /\d{1,2}(fed|ain|af|il|ydd|ed|eg)/,
        // traditional ordinal numbers above 31 are not commonly used in colloquial Welsh
        ordinal: function (number) {
            var b = number,
                output = '',
                lookup = [
                    '', 'af', 'il', 'ydd', 'ydd', 'ed', 'ed', 'ed', 'fed', 'fed', 'fed', // 1af to 10fed
                    'eg', 'fed', 'eg', 'eg', 'fed', 'eg', 'eg', 'fed', 'eg', 'fed' // 11eg to 20fed
                ];

            if (b > 20) {
                if (b === 40 || b === 50 || b === 60 || b === 80 || b === 100) {
                    output = 'fed'; // not 30ain, 70ain or 90ain
                } else {
                    output = 'ain';
                }
            } else if (b > 0) {
                output = lookup[b];
            }

            return number + output;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : danish (da)
// author : Ulrik Nielsen : https://github.com/mrbase

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('da', {
        months : 'januar_februar_marts_april_maj_juni_juli_august_september_oktober_november_december'.split('_'),
        monthsShort : 'jan_feb_mar_apr_maj_jun_jul_aug_sep_okt_nov_dec'.split('_'),
        weekdays : 's첩ndag_mandag_tirsdag_onsdag_torsdag_fredag_l첩rdag'.split('_'),
        weekdaysShort : 's첩n_man_tir_ons_tor_fre_l첩r'.split('_'),
        weekdaysMin : 's첩_ma_ti_on_to_fr_l첩'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY LT',
            LLLL : 'dddd [d.] D. MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[I dag kl.] LT',
            nextDay : '[I morgen kl.] LT',
            nextWeek : 'dddd [kl.] LT',
            lastDay : '[I g책r kl.] LT',
            lastWeek : '[sidste] dddd [kl] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'om %s',
            past : '%s siden',
            s : 'f책 sekunder',
            m : 'et minut',
            mm : '%d minutter',
            h : 'en time',
            hh : '%d timer',
            d : 'en dag',
            dd : '%d dage',
            M : 'en m책ned',
            MM : '%d m책neder',
            y : 'et 책r',
            yy : '%d 책r'
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : austrian german (de-at)
// author : lluchs : https://github.com/lluchs
// author: Menelion Elens첬le: https://github.com/Oire
// author : Martin Groller : https://github.com/MadMG

(function (factory) {
    factory(moment);
}(function (moment) {
    function processRelativeTime(number, withoutSuffix, key, isFuture) {
        var format = {
            'm': ['eine Minute', 'einer Minute'],
            'h': ['eine Stunde', 'einer Stunde'],
            'd': ['ein Tag', 'einem Tag'],
            'dd': [number + ' Tage', number + ' Tagen'],
            'M': ['ein Monat', 'einem Monat'],
            'MM': [number + ' Monate', number + ' Monaten'],
            'y': ['ein Jahr', 'einem Jahr'],
            'yy': [number + ' Jahre', number + ' Jahren']
        };
        return withoutSuffix ? format[key][0] : format[key][1];
    }

    return moment.defineLocale('de-at', {
        months : 'J채nner_Februar_M채rz_April_Mai_Juni_Juli_August_September_Oktober_November_Dezember'.split('_'),
        monthsShort : 'J채n._Febr._Mrz._Apr._Mai_Jun._Jul._Aug._Sept._Okt._Nov._Dez.'.split('_'),
        weekdays : 'Sonntag_Montag_Dienstag_Mittwoch_Donnerstag_Freitag_Samstag'.split('_'),
        weekdaysShort : 'So._Mo._Di._Mi._Do._Fr._Sa.'.split('_'),
        weekdaysMin : 'So_Mo_Di_Mi_Do_Fr_Sa'.split('_'),
        longDateFormat : {
            LT: 'HH:mm',
            LTS: 'HH:mm:ss',
            L : 'DD.MM.YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY LT',
            LLLL : 'dddd, D. MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[Heute um] LT [Uhr]',
            sameElse: 'L',
            nextDay: '[Morgen um] LT [Uhr]',
            nextWeek: 'dddd [um] LT [Uhr]',
            lastDay: '[Gestern um] LT [Uhr]',
            lastWeek: '[letzten] dddd [um] LT [Uhr]'
        },
        relativeTime : {
            future : 'in %s',
            past : 'vor %s',
            s : 'ein paar Sekunden',
            m : processRelativeTime,
            mm : '%d Minuten',
            h : processRelativeTime,
            hh : '%d Stunden',
            d : processRelativeTime,
            dd : processRelativeTime,
            M : processRelativeTime,
            MM : processRelativeTime,
            y : processRelativeTime,
            yy : processRelativeTime
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : german (de)
// author : lluchs : https://github.com/lluchs
// author: Menelion Elens첬le: https://github.com/Oire

(function (factory) {
    factory(moment);
}(function (moment) {
    function processRelativeTime(number, withoutSuffix, key, isFuture) {
        var format = {
            'm': ['eine Minute', 'einer Minute'],
            'h': ['eine Stunde', 'einer Stunde'],
            'd': ['ein Tag', 'einem Tag'],
            'dd': [number + ' Tage', number + ' Tagen'],
            'M': ['ein Monat', 'einem Monat'],
            'MM': [number + ' Monate', number + ' Monaten'],
            'y': ['ein Jahr', 'einem Jahr'],
            'yy': [number + ' Jahre', number + ' Jahren']
        };
        return withoutSuffix ? format[key][0] : format[key][1];
    }

    return moment.defineLocale('de', {
        months : 'Januar_Februar_M채rz_April_Mai_Juni_Juli_August_September_Oktober_November_Dezember'.split('_'),
        monthsShort : 'Jan._Febr._Mrz._Apr._Mai_Jun._Jul._Aug._Sept._Okt._Nov._Dez.'.split('_'),
        weekdays : 'Sonntag_Montag_Dienstag_Mittwoch_Donnerstag_Freitag_Samstag'.split('_'),
        weekdaysShort : 'So._Mo._Di._Mi._Do._Fr._Sa.'.split('_'),
        weekdaysMin : 'So_Mo_Di_Mi_Do_Fr_Sa'.split('_'),
        longDateFormat : {
            LT: 'HH:mm',
            LTS: 'HH:mm:ss',
            L : 'DD.MM.YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY LT',
            LLLL : 'dddd, D. MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[Heute um] LT [Uhr]',
            sameElse: 'L',
            nextDay: '[Morgen um] LT [Uhr]',
            nextWeek: 'dddd [um] LT [Uhr]',
            lastDay: '[Gestern um] LT [Uhr]',
            lastWeek: '[letzten] dddd [um] LT [Uhr]'
        },
        relativeTime : {
            future : 'in %s',
            past : 'vor %s',
            s : 'ein paar Sekunden',
            m : processRelativeTime,
            mm : '%d Minuten',
            h : processRelativeTime,
            hh : '%d Stunden',
            d : processRelativeTime,
            dd : processRelativeTime,
            M : processRelativeTime,
            MM : processRelativeTime,
            y : processRelativeTime,
            yy : processRelativeTime
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : modern greek (el)
// author : Aggelos Karalias : https://github.com/mehiel

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('el', {
        monthsNominativeEl : '�慣館恝�郭�菅恝�_過琯棺�恝�郭�菅恝�_�郭��菅恝�_���官貫菅恝�_�郭菅恝�_�恝�館菅恝�_�恝�貫菅恝�_��款恝���恝�_誇琯��串關棺�菅恝�_�觀��棺�菅恝�_�恝串關棺�菅恝�_�琯觀串關棺�菅恝�'.split('_'),
        monthsGenitiveEl : '�慣館恝�慣�官恝�_過琯棺�恝�慣�官恝�_�慣��官恝�_���菅貫官恝�_�慣�恝�_�恝�館官恝�_�恝�貫官恝�_��款恝���恝�_誇琯��琯關棺�官恝�_�觀��棺�官恝�_�恝琯關棺�官恝�_�琯觀琯關棺�官恝�'.split('_'),
        months : function (momentToFormat, format) {
            if (/D/.test(format.substring(0, format.indexOf('MMMM')))) { // if there is a day number before 'MMMM'
                return this._monthsGenitiveEl[momentToFormat.month()];
            } else {
                return this._monthsNominativeEl[momentToFormat.month()];
            }
        },
        monthsShort : '�慣館_過琯棺_�慣�_���_�慣�_�恝�館_�恝�貫_��款_誇琯�_�觀�_�恝琯_�琯觀'.split('_'),
        weekdays : '���菅慣觀冠_�琯��串�慣_課�官�管_課琯�郭��管_�串關��管_�慣�慣�觀琯�冠_誇郭棺棺慣�恝'.split('_'),
        weekdaysShort : '���_�琯�_課�菅_課琯�_�琯關_�慣�_誇慣棺'.split('_'),
        weekdaysMin : '��_�琯_課�_課琯_�琯_�慣_誇慣'.split('_'),
        meridiem : function (hours, minutes, isLower) {
            if (hours > 11) {
                return isLower ? '關關' : '��';
            } else {
                return isLower ? '�關' : '��';
            }
        },
        isPM : function (input) {
            return ((input + '').toLowerCase()[0] === '關');
        },
        meridiemParse : /[��]\.?�?\.?/i,
        longDateFormat : {
            LT : 'h:mm A',
            LTS : 'h:mm:ss A',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendarEl : {
            sameDay : '[誇冠關琯�慣 {}] LT',
            nextDay : '[���菅恝 {}] LT',
            nextWeek : 'dddd [{}] LT',
            lastDay : '[鍋罐琯� {}] LT',
            lastWeek : function () {
                switch (this.day()) {
                    case 6:
                        return '[�恝 ��恝管款恝�關琯館恝] dddd [{}] LT';
                    default:
                        return '[�管館 ��恝管款恝�關琯館管] dddd [{}] LT';
                }
            },
            sameElse : 'L'
        },
        calendar : function (key, mom) {
            var output = this._calendarEl[key],
                hours = mom && mom.hours();

            if (typeof output === 'function') {
                output = output.apply(mom);
            }

            return output.replace('{}', (hours % 12 === 1 ? '��管' : '��菅�'));
        },
        relativeTime : {
            future : '�琯 %s',
            past : '%s ��菅館',
            s : '貫官款慣 灌琯��琯��貫琯��慣',
            m : '串館慣 貫琯���',
            mm : '%d 貫琯��郭',
            h : '關官慣 ��慣',
            hh : '%d ��琯�',
            d : '關官慣 關串�慣',
            dd : '%d 關串�琯�',
            M : '串館慣� 關冠館慣�',
            MM : '%d 關冠館琯�',
            y : '串館慣� ���館恝�',
            yy : '%d ���館菅慣'
        },
        ordinalParse: /\d{1,2}管/,
        ordinal: '%d管',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : australian english (en-au)

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('en-au', {
        months : 'January_February_March_April_May_June_July_August_September_October_November_December'.split('_'),
        monthsShort : 'Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec'.split('_'),
        weekdays : 'Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday'.split('_'),
        weekdaysShort : 'Sun_Mon_Tue_Wed_Thu_Fri_Sat'.split('_'),
        weekdaysMin : 'Su_Mo_Tu_We_Th_Fr_Sa'.split('_'),
        longDateFormat : {
            LT : 'h:mm A',
            LTS : 'h:mm:ss A',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[Today at] LT',
            nextDay : '[Tomorrow at] LT',
            nextWeek : 'dddd [at] LT',
            lastDay : '[Yesterday at] LT',
            lastWeek : '[Last] dddd [at] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'in %s',
            past : '%s ago',
            s : 'a few seconds',
            m : 'a minute',
            mm : '%d minutes',
            h : 'an hour',
            hh : '%d hours',
            d : 'a day',
            dd : '%d days',
            M : 'a month',
            MM : '%d months',
            y : 'a year',
            yy : '%d years'
        },
        ordinalParse: /\d{1,2}(st|nd|rd|th)/,
        ordinal : function (number) {
            var b = number % 10,
                output = (~~(number % 100 / 10) === 1) ? 'th' :
                (b === 1) ? 'st' :
                (b === 2) ? 'nd' :
                (b === 3) ? 'rd' : 'th';
            return number + output;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : canadian english (en-ca)
// author : Jonathan Abourbih : https://github.com/jonbca

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('en-ca', {
        months : 'January_February_March_April_May_June_July_August_September_October_November_December'.split('_'),
        monthsShort : 'Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec'.split('_'),
        weekdays : 'Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday'.split('_'),
        weekdaysShort : 'Sun_Mon_Tue_Wed_Thu_Fri_Sat'.split('_'),
        weekdaysMin : 'Su_Mo_Tu_We_Th_Fr_Sa'.split('_'),
        longDateFormat : {
            LT : 'h:mm A',
            LTS : 'h:mm:ss A',
            L : 'YYYY-MM-DD',
            LL : 'D MMMM, YYYY',
            LLL : 'D MMMM, YYYY LT',
            LLLL : 'dddd, D MMMM, YYYY LT'
        },
        calendar : {
            sameDay : '[Today at] LT',
            nextDay : '[Tomorrow at] LT',
            nextWeek : 'dddd [at] LT',
            lastDay : '[Yesterday at] LT',
            lastWeek : '[Last] dddd [at] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'in %s',
            past : '%s ago',
            s : 'a few seconds',
            m : 'a minute',
            mm : '%d minutes',
            h : 'an hour',
            hh : '%d hours',
            d : 'a day',
            dd : '%d days',
            M : 'a month',
            MM : '%d months',
            y : 'a year',
            yy : '%d years'
        },
        ordinalParse: /\d{1,2}(st|nd|rd|th)/,
        ordinal : function (number) {
            var b = number % 10,
                output = (~~(number % 100 / 10) === 1) ? 'th' :
                (b === 1) ? 'st' :
                (b === 2) ? 'nd' :
                (b === 3) ? 'rd' : 'th';
            return number + output;
        }
    });
}));
// moment.js locale configuration
// locale : great britain english (en-gb)
// author : Chris Gedrim : https://github.com/chrisgedrim

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('en-gb', {
        months : 'January_February_March_April_May_June_July_August_September_October_November_December'.split('_'),
        monthsShort : 'Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec'.split('_'),
        weekdays : 'Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday'.split('_'),
        weekdaysShort : 'Sun_Mon_Tue_Wed_Thu_Fri_Sat'.split('_'),
        weekdaysMin : 'Su_Mo_Tu_We_Th_Fr_Sa'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'HH:mm:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[Today at] LT',
            nextDay : '[Tomorrow at] LT',
            nextWeek : 'dddd [at] LT',
            lastDay : '[Yesterday at] LT',
            lastWeek : '[Last] dddd [at] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'in %s',
            past : '%s ago',
            s : 'a few seconds',
            m : 'a minute',
            mm : '%d minutes',
            h : 'an hour',
            hh : '%d hours',
            d : 'a day',
            dd : '%d days',
            M : 'a month',
            MM : '%d months',
            y : 'a year',
            yy : '%d years'
        },
        ordinalParse: /\d{1,2}(st|nd|rd|th)/,
        ordinal : function (number) {
            var b = number % 10,
                output = (~~(number % 100 / 10) === 1) ? 'th' :
                (b === 1) ? 'st' :
                (b === 2) ? 'nd' :
                (b === 3) ? 'rd' : 'th';
            return number + output;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : esperanto (eo)
// author : Colin Dean : https://github.com/colindean
// komento: Mi estas malcerta se mi korekte traktis akuzativojn en tiu traduko.
//          Se ne, bonvolu korekti kaj avizi min por ke mi povas lerni!

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('eo', {
        months : 'januaro_februaro_marto_aprilo_majo_junio_julio_a큼gusto_septembro_oktobro_novembro_decembro'.split('_'),
        monthsShort : 'jan_feb_mar_apr_maj_jun_jul_a큼g_sep_okt_nov_dec'.split('_'),
        weekdays : 'Diman훸o_Lundo_Mardo_Merkredo_캑a큼do_Vendredo_Sabato'.split('_'),
        weekdaysShort : 'Dim_Lun_Mard_Merk_캑a큼_Ven_Sab'.split('_'),
        weekdaysMin : 'Di_Lu_Ma_Me_캑a_Ve_Sa'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'YYYY-MM-DD',
            LL : 'D[-an de] MMMM, YYYY',
            LLL : 'D[-an de] MMMM, YYYY LT',
            LLLL : 'dddd, [la] D[-an de] MMMM, YYYY LT'
        },
        meridiemParse: /[ap]\.t\.m/i,
        isPM: function (input) {
            return input.charAt(0).toLowerCase() === 'p';
        },
        meridiem : function (hours, minutes, isLower) {
            if (hours > 11) {
                return isLower ? 'p.t.m.' : 'P.T.M.';
            } else {
                return isLower ? 'a.t.m.' : 'A.T.M.';
            }
        },
        calendar : {
            sameDay : '[Hodia큼 je] LT',
            nextDay : '[Morga큼 je] LT',
            nextWeek : 'dddd [je] LT',
            lastDay : '[Hiera큼 je] LT',
            lastWeek : '[pasinta] dddd [je] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'je %s',
            past : 'anta큼 %s',
            s : 'sekundoj',
            m : 'minuto',
            mm : '%d minutoj',
            h : 'horo',
            hh : '%d horoj',
            d : 'tago',//ne 'diurno', 훸ar estas uzita por proksimumo
            dd : '%d tagoj',
            M : 'monato',
            MM : '%d monatoj',
            y : 'jaro',
            yy : '%d jaroj'
        },
        ordinalParse: /\d{1,2}a/,
        ordinal : '%da',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : spanish (es)
// author : Julio Napur챠 : https://github.com/julionc

(function (factory) {
    factory(moment);
}(function (moment) {
    var monthsShortDot = 'ene._feb._mar._abr._may._jun._jul._ago._sep._oct._nov._dic.'.split('_'),
        monthsShort = 'ene_feb_mar_abr_may_jun_jul_ago_sep_oct_nov_dic'.split('_');

    return moment.defineLocale('es', {
        months : 'enero_febrero_marzo_abril_mayo_junio_julio_agosto_septiembre_octubre_noviembre_diciembre'.split('_'),
        monthsShort : function (m, format) {
            if (/-MMM-/.test(format)) {
                return monthsShort[m.month()];
            } else {
                return monthsShortDot[m.month()];
            }
        },
        weekdays : 'domingo_lunes_martes_mi챕rcoles_jueves_viernes_s찼bado'.split('_'),
        weekdaysShort : 'dom._lun._mar._mi챕._jue._vie._s찼b.'.split('_'),
        weekdaysMin : 'Do_Lu_Ma_Mi_Ju_Vi_S찼'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D [de] MMMM [de] YYYY',
            LLL : 'D [de] MMMM [de] YYYY LT',
            LLLL : 'dddd, D [de] MMMM [de] YYYY LT'
        },
        calendar : {
            sameDay : function () {
                return '[hoy a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            nextDay : function () {
                return '[ma챰ana a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            nextWeek : function () {
                return 'dddd [a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            lastDay : function () {
                return '[ayer a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            lastWeek : function () {
                return '[el] dddd [pasado a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : 'en %s',
            past : 'hace %s',
            s : 'unos segundos',
            m : 'un minuto',
            mm : '%d minutos',
            h : 'una hora',
            hh : '%d horas',
            d : 'un d챠a',
            dd : '%d d챠as',
            M : 'un mes',
            MM : '%d meses',
            y : 'un a챰o',
            yy : '%d a챰os'
        },
        ordinalParse : /\d{1,2}쨘/,
        ordinal : '%d쨘',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : estonian (et)
// author : Henry Kehlmann : https://github.com/madhenry
// improvements : Illimar Tambek : https://github.com/ragulka

(function (factory) {
    factory(moment);
}(function (moment) {
    function processRelativeTime(number, withoutSuffix, key, isFuture) {
        var format = {
            's' : ['m천ne sekundi', 'm천ni sekund', 'paar sekundit'],
            'm' : ['체he minuti', '체ks minut'],
            'mm': [number + ' minuti', number + ' minutit'],
            'h' : ['체he tunni', 'tund aega', '체ks tund'],
            'hh': [number + ' tunni', number + ' tundi'],
            'd' : ['체he p채eva', '체ks p채ev'],
            'M' : ['kuu aja', 'kuu aega', '체ks kuu'],
            'MM': [number + ' kuu', number + ' kuud'],
            'y' : ['체he aasta', 'aasta', '체ks aasta'],
            'yy': [number + ' aasta', number + ' aastat']
        };
        if (withoutSuffix) {
            return format[key][2] ? format[key][2] : format[key][1];
        }
        return isFuture ? format[key][0] : format[key][1];
    }

    return moment.defineLocale('et', {
        months        : 'jaanuar_veebruar_m채rts_aprill_mai_juuni_juuli_august_september_oktoober_november_detsember'.split('_'),
        monthsShort   : 'jaan_veebr_m채rts_apr_mai_juuni_juuli_aug_sept_okt_nov_dets'.split('_'),
        weekdays      : 'p체hap채ev_esmasp채ev_teisip채ev_kolmap채ev_neljap채ev_reede_laup채ev'.split('_'),
        weekdaysShort : 'P_E_T_K_N_R_L'.split('_'),
        weekdaysMin   : 'P_E_T_K_N_R_L'.split('_'),
        longDateFormat : {
            LT   : 'H:mm',
            LTS : 'LT:ss',
            L    : 'DD.MM.YYYY',
            LL   : 'D. MMMM YYYY',
            LLL  : 'D. MMMM YYYY LT',
            LLLL : 'dddd, D. MMMM YYYY LT'
        },
        calendar : {
            sameDay  : '[T채na,] LT',
            nextDay  : '[Homme,] LT',
            nextWeek : '[J채rgmine] dddd LT',
            lastDay  : '[Eile,] LT',
            lastWeek : '[Eelmine] dddd LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s p채rast',
            past   : '%s tagasi',
            s      : processRelativeTime,
            m      : processRelativeTime,
            mm     : processRelativeTime,
            h      : processRelativeTime,
            hh     : processRelativeTime,
            d      : processRelativeTime,
            dd     : '%d p채eva',
            M      : processRelativeTime,
            MM     : processRelativeTime,
            y      : processRelativeTime,
            yy     : processRelativeTime
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : euskara (eu)
// author : Eneko Illarramendi : https://github.com/eillarra

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('eu', {
        months : 'urtarrila_otsaila_martxoa_apirila_maiatza_ekaina_uztaila_abuztua_iraila_urria_azaroa_abendua'.split('_'),
        monthsShort : 'urt._ots._mar._api._mai._eka._uzt._abu._ira._urr._aza._abe.'.split('_'),
        weekdays : 'igandea_astelehena_asteartea_asteazkena_osteguna_ostirala_larunbata'.split('_'),
        weekdaysShort : 'ig._al._ar._az._og._ol._lr.'.split('_'),
        weekdaysMin : 'ig_al_ar_az_og_ol_lr'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'YYYY-MM-DD',
            LL : 'YYYY[ko] MMMM[ren] D[a]',
            LLL : 'YYYY[ko] MMMM[ren] D[a] LT',
            LLLL : 'dddd, YYYY[ko] MMMM[ren] D[a] LT',
            l : 'YYYY-M-D',
            ll : 'YYYY[ko] MMM D[a]',
            lll : 'YYYY[ko] MMM D[a] LT',
            llll : 'ddd, YYYY[ko] MMM D[a] LT'
        },
        calendar : {
            sameDay : '[gaur] LT[etan]',
            nextDay : '[bihar] LT[etan]',
            nextWeek : 'dddd LT[etan]',
            lastDay : '[atzo] LT[etan]',
            lastWeek : '[aurreko] dddd LT[etan]',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s barru',
            past : 'duela %s',
            s : 'segundo batzuk',
            m : 'minutu bat',
            mm : '%d minutu',
            h : 'ordu bat',
            hh : '%d ordu',
            d : 'egun bat',
            dd : '%d egun',
            M : 'hilabete bat',
            MM : '%d hilabete',
            y : 'urte bat',
            yy : '%d urte'
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Persian (fa)
// author : Ebrahim Byagowi : https://github.com/ebraminio

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '旁',
        '2': '昉',
        '3': '枋',
        '4': '榜',
        '5': '滂',
        '6': '磅',
        '7': '紡',
        '8': '肪',
        '9': '膀',
        '0': '方'
    }, numberMap = {
        '旁': '1',
        '昉': '2',
        '枋': '3',
        '榜': '4',
        '滂': '5',
        '磅': '6',
        '紡': '7',
        '肪': '8',
        '膀': '9',
        '方': '0'
    };

    return moment.defineLocale('fa', {
        months : '�碼����_��邈��_�碼邈卍_笠�邈��_��_��痲�_��痲��_碼�魔_卍毛魔碼�磨邈_碼沕魔磨邈_��碼�磨邈_膜卍碼�磨邈'.split('_'),
        monthsShort : '�碼����_��邈��_�碼邈卍_笠�邈��_��_��痲�_��痲��_碼�魔_卍毛魔碼�磨邈_碼沕魔磨邈_��碼�磨邈_膜卍碼�磨邈'.split('_'),
        weekdays : '�沕\u200c娩�磨�_膜�娩�磨�_卍�\u200c娩�磨�_��碼邈娩�磨�_毛�寞\u200c娩�磨�_寞�晩�_娩�磨�'.split('_'),
        weekdaysShort : '�沕\u200c娩�磨�_膜�娩�磨�_卍�\u200c娩�磨�_��碼邈娩�磨�_毛�寞\u200c娩�磨�_寞�晩�_娩�磨�'.split('_'),
        weekdaysMin : '�_膜_卍_�_毛_寞_娩'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        meridiemParse: /�磨� 碼万 挽�邈|磨晩膜 碼万 挽�邈/,
        isPM: function (input) {
            return /磨晩膜 碼万 挽�邈/.test(input);
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 12) {
                return '�磨� 碼万 挽�邈';
            } else {
                return '磨晩膜 碼万 挽�邈';
            }
        },
        calendar : {
            sameDay : '[碼�邈�万 卍碼晩魔] LT',
            nextDay : '[�邈膜碼 卍碼晩魔] LT',
            nextWeek : 'dddd [卍碼晩魔] LT',
            lastDay : '[膜�邈�万 卍碼晩魔] LT',
            lastWeek : 'dddd [毛�娩] [卍碼晩魔] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '膜邈 %s',
            past : '%s 毛�娩',
            s : '��膜�� 麻碼���',
            m : '�沕 膜����',
            mm : '%d 膜����',
            h : '�沕 卍碼晩魔',
            hh : '%d 卍碼晩魔',
            d : '�沕 邈�万',
            dd : '%d 邈�万',
            M : '�沕 �碼�',
            MM : '%d �碼�',
            y : '�沕 卍碼�',
            yy : '%d 卍碼�'
        },
        preparse: function (string) {
            return string.replace(/[方-膀]/g, function (match) {
                return numberMap[match];
            }).replace(/�/g, ',');
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            }).replace(/,/g, '�');
        },
        ordinalParse: /\d{1,2}�/,
        ordinal : '%d�',
        week : {
            dow : 6, // Saturday is the first day of the week.
            doy : 12 // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : finnish (fi)
// author : Tarmo Aidantausta : https://github.com/bleadof

(function (factory) {
    factory(moment);
}(function (moment) {
    var numbersPast = 'nolla yksi kaksi kolme nelj채 viisi kuusi seitsem채n kahdeksan yhdeks채n'.split(' '),
        numbersFuture = [
            'nolla', 'yhden', 'kahden', 'kolmen', 'nelj채n', 'viiden', 'kuuden',
            numbersPast[7], numbersPast[8], numbersPast[9]
        ];

    function translate(number, withoutSuffix, key, isFuture) {
        var result = '';
        switch (key) {
        case 's':
            return isFuture ? 'muutaman sekunnin' : 'muutama sekunti';
        case 'm':
            return isFuture ? 'minuutin' : 'minuutti';
        case 'mm':
            result = isFuture ? 'minuutin' : 'minuuttia';
            break;
        case 'h':
            return isFuture ? 'tunnin' : 'tunti';
        case 'hh':
            result = isFuture ? 'tunnin' : 'tuntia';
            break;
        case 'd':
            return isFuture ? 'p채iv채n' : 'p채iv채';
        case 'dd':
            result = isFuture ? 'p채iv채n' : 'p채iv채채';
            break;
        case 'M':
            return isFuture ? 'kuukauden' : 'kuukausi';
        case 'MM':
            result = isFuture ? 'kuukauden' : 'kuukautta';
            break;
        case 'y':
            return isFuture ? 'vuoden' : 'vuosi';
        case 'yy':
            result = isFuture ? 'vuoden' : 'vuotta';
            break;
        }
        result = verbalNumber(number, isFuture) + ' ' + result;
        return result;
    }

    function verbalNumber(number, isFuture) {
        return number < 10 ? (isFuture ? numbersFuture[number] : numbersPast[number]) : number;
    }

    return moment.defineLocale('fi', {
        months : 'tammikuu_helmikuu_maaliskuu_huhtikuu_toukokuu_kes채kuu_hein채kuu_elokuu_syyskuu_lokakuu_marraskuu_joulukuu'.split('_'),
        monthsShort : 'tammi_helmi_maalis_huhti_touko_kes채_hein채_elo_syys_loka_marras_joulu'.split('_'),
        weekdays : 'sunnuntai_maanantai_tiistai_keskiviikko_torstai_perjantai_lauantai'.split('_'),
        weekdaysShort : 'su_ma_ti_ke_to_pe_la'.split('_'),
        weekdaysMin : 'su_ma_ti_ke_to_pe_la'.split('_'),
        longDateFormat : {
            LT : 'HH.mm',
            LTS : 'HH.mm.ss',
            L : 'DD.MM.YYYY',
            LL : 'Do MMMM[ta] YYYY',
            LLL : 'Do MMMM[ta] YYYY, [klo] LT',
            LLLL : 'dddd, Do MMMM[ta] YYYY, [klo] LT',
            l : 'D.M.YYYY',
            ll : 'Do MMM YYYY',
            lll : 'Do MMM YYYY, [klo] LT',
            llll : 'ddd, Do MMM YYYY, [klo] LT'
        },
        calendar : {
            sameDay : '[t채n채채n] [klo] LT',
            nextDay : '[huomenna] [klo] LT',
            nextWeek : 'dddd [klo] LT',
            lastDay : '[eilen] [klo] LT',
            lastWeek : '[viime] dddd[na] [klo] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s p채채st채',
            past : '%s sitten',
            s : translate,
            m : translate,
            mm : translate,
            h : translate,
            hh : translate,
            d : translate,
            dd : translate,
            M : translate,
            MM : translate,
            y : translate,
            yy : translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : faroese (fo)
// author : Ragnar Johannesen : https://github.com/ragnar123

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('fo', {
        months : 'januar_februar_mars_apr챠l_mai_juni_juli_august_september_oktober_november_desember'.split('_'),
        monthsShort : 'jan_feb_mar_apr_mai_jun_jul_aug_sep_okt_nov_des'.split('_'),
        weekdays : 'sunnudagur_m찼nadagur_t첵sdagur_mikudagur_h처sdagur_fr챠ggjadagur_leygardagur'.split('_'),
        weekdaysShort : 'sun_m찼n_t첵s_mik_h처s_fr챠_ley'.split('_'),
        weekdaysMin : 'su_m찼_t첵_mi_h처_fr_le'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D. MMMM, YYYY LT'
        },
        calendar : {
            sameDay : '[횒 dag kl.] LT',
            nextDay : '[횒 morgin kl.] LT',
            nextWeek : 'dddd [kl.] LT',
            lastDay : '[횒 gj찼r kl.] LT',
            lastWeek : '[s챠챨stu] dddd [kl] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'um %s',
            past : '%s s챠챨ani',
            s : 'f찼 sekund',
            m : 'ein minutt',
            mm : '%d minuttir',
            h : 'ein t챠mi',
            hh : '%d t챠mar',
            d : 'ein dagur',
            dd : '%d dagar',
            M : 'ein m찼na챨i',
            MM : '%d m찼na챨ir',
            y : 'eitt 찼r',
            yy : '%d 찼r'
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : canadian french (fr-ca)
// author : Jonathan Abourbih : https://github.com/jonbca

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('fr-ca', {
        months : 'janvier_f챕vrier_mars_avril_mai_juin_juillet_ao청t_septembre_octobre_novembre_d챕cembre'.split('_'),
        monthsShort : 'janv._f챕vr._mars_avr._mai_juin_juil._ao청t_sept._oct._nov._d챕c.'.split('_'),
        weekdays : 'dimanche_lundi_mardi_mercredi_jeudi_vendredi_samedi'.split('_'),
        weekdaysShort : 'dim._lun._mar._mer._jeu._ven._sam.'.split('_'),
        weekdaysMin : 'Di_Lu_Ma_Me_Je_Ve_Sa'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'YYYY-MM-DD',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[Aujourd\'hui 횪] LT',
            nextDay: '[Demain 횪] LT',
            nextWeek: 'dddd [횪] LT',
            lastDay: '[Hier 횪] LT',
            lastWeek: 'dddd [dernier 횪] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'dans %s',
            past : 'il y a %s',
            s : 'quelques secondes',
            m : 'une minute',
            mm : '%d minutes',
            h : 'une heure',
            hh : '%d heures',
            d : 'un jour',
            dd : '%d jours',
            M : 'un mois',
            MM : '%d mois',
            y : 'un an',
            yy : '%d ans'
        },
        ordinalParse: /\d{1,2}(er|)/,
        ordinal : function (number) {
            return number + (number === 1 ? 'er' : '');
        }
    });
}));
// moment.js locale configuration
// locale : french (fr)
// author : John Fischer : https://github.com/jfroffice

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('fr', {
        months : 'janvier_f챕vrier_mars_avril_mai_juin_juillet_ao청t_septembre_octobre_novembre_d챕cembre'.split('_'),
        monthsShort : 'janv._f챕vr._mars_avr._mai_juin_juil._ao청t_sept._oct._nov._d챕c.'.split('_'),
        weekdays : 'dimanche_lundi_mardi_mercredi_jeudi_vendredi_samedi'.split('_'),
        weekdaysShort : 'dim._lun._mar._mer._jeu._ven._sam.'.split('_'),
        weekdaysMin : 'Di_Lu_Ma_Me_Je_Ve_Sa'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[Aujourd\'hui 횪] LT',
            nextDay: '[Demain 횪] LT',
            nextWeek: 'dddd [횪] LT',
            lastDay: '[Hier 횪] LT',
            lastWeek: 'dddd [dernier 횪] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'dans %s',
            past : 'il y a %s',
            s : 'quelques secondes',
            m : 'une minute',
            mm : '%d minutes',
            h : 'une heure',
            hh : '%d heures',
            d : 'un jour',
            dd : '%d jours',
            M : 'un mois',
            MM : '%d mois',
            y : 'un an',
            yy : '%d ans'
        },
        ordinalParse: /\d{1,2}(er|)/,
        ordinal : function (number) {
            return number + (number === 1 ? 'er' : '');
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : frisian (fy)
// author : Robin van der Vliet : https://github.com/robin0van0der0v

(function (factory) {
    factory(moment);
}(function (moment) {
    var monthsShortWithDots = 'jan._feb._mrt._apr._mai_jun._jul._aug._sep._okt._nov._des.'.split('_'),
        monthsShortWithoutDots = 'jan_feb_mrt_apr_mai_jun_jul_aug_sep_okt_nov_des'.split('_');

    return moment.defineLocale('fy', {
        months : 'jannewaris_febrewaris_maart_april_maaie_juny_july_augustus_septimber_oktober_novimber_desimber'.split('_'),
        monthsShort : function (m, format) {
            if (/-MMM-/.test(format)) {
                return monthsShortWithoutDots[m.month()];
            } else {
                return monthsShortWithDots[m.month()];
            }
        },
        weekdays : 'snein_moandei_tiisdei_woansdei_tongersdei_freed_sneon'.split('_'),
        weekdaysShort : 'si._mo._ti._wo._to._fr._so.'.split('_'),
        weekdaysMin : 'Si_Mo_Ti_Wo_To_Fr_So'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD-MM-YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[hjoed om] LT',
            nextDay: '[moarn om] LT',
            nextWeek: 'dddd [om] LT',
            lastDay: '[juster om] LT',
            lastWeek: '[척fr청ne] dddd [om] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'oer %s',
            past : '%s lyn',
            s : 'in pear sekonden',
            m : 'ien min첬t',
            mm : '%d minuten',
            h : 'ien oere',
            hh : '%d oeren',
            d : 'ien dei',
            dd : '%d dagen',
            M : 'ien moanne',
            MM : '%d moannen',
            y : 'ien jier',
            yy : '%d jierren'
        },
        ordinalParse: /\d{1,2}(ste|de)/,
        ordinal : function (number) {
            return number + ((number === 1 || number === 8 || number >= 20) ? 'ste' : 'de');
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : galician (gl)
// author : Juan G. Hurtado : https://github.com/juanghurtado

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('gl', {
        months : 'Xaneiro_Febreiro_Marzo_Abril_Maio_Xu챰o_Xullo_Agosto_Setembro_Outubro_Novembro_Decembro'.split('_'),
        monthsShort : 'Xan._Feb._Mar._Abr._Mai._Xu챰._Xul._Ago._Set._Out._Nov._Dec.'.split('_'),
        weekdays : 'Domingo_Luns_Martes_M챕rcores_Xoves_Venres_S찼bado'.split('_'),
        weekdaysShort : 'Dom._Lun._Mar._M챕r._Xov._Ven._S찼b.'.split('_'),
        weekdaysMin : 'Do_Lu_Ma_M챕_Xo_Ve_S찼'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay : function () {
                return '[hoxe ' + ((this.hours() !== 1) ? '찼s' : '찼') + '] LT';
            },
            nextDay : function () {
                return '[ma챰찼 ' + ((this.hours() !== 1) ? '찼s' : '찼') + '] LT';
            },
            nextWeek : function () {
                return 'dddd [' + ((this.hours() !== 1) ? '찼s' : 'a') + '] LT';
            },
            lastDay : function () {
                return '[onte ' + ((this.hours() !== 1) ? '찼' : 'a') + '] LT';
            },
            lastWeek : function () {
                return '[o] dddd [pasado ' + ((this.hours() !== 1) ? '찼s' : 'a') + '] LT';
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : function (str) {
                if (str === 'uns segundos') {
                    return 'nuns segundos';
                }
                return 'en ' + str;
            },
            past : 'hai %s',
            s : 'uns segundos',
            m : 'un minuto',
            mm : '%d minutos',
            h : 'unha hora',
            hh : '%d horas',
            d : 'un d챠a',
            dd : '%d d챠as',
            M : 'un mes',
            MM : '%d meses',
            y : 'un ano',
            yy : '%d anos'
        },
        ordinalParse : /\d{1,2}쨘/,
        ordinal : '%d쨘',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Hebrew (he)
// author : Tomer Cohen : https://github.com/tomer
// author : Moshe Simantov : https://github.com/DevelopmentIL
// author : Tal Ater : https://github.com/TalAter

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('he', {
        months : '����淚_壘�淚��淚_�淚婁_�壘淚��_���_����_����_����遼�_遼壘���淚_��樓���淚_�����淚_�屢��淚'.split('_'),
        monthsShort : '���柳_壘�淚柳_�淚婁_�壘淚柳_���_����_����_���柳_遼壘�柳_��樓柳_���柳_�屢�柳'.split('_'),
        weekdays : '淚�漏��_漏��_漏��漏�_淚��鬧�_���漏�_漏�漏�_漏�瘻'.split('_'),
        weekdaysShort : '�柳_�柳_�柳_�柳_�柳_�柳_漏柳'.split('_'),
        weekdaysMin : '�_�_�_�_�_�_漏'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D [�]MMMM YYYY',
            LLL : 'D [�]MMMM YYYY LT',
            LLLL : 'dddd, D [�]MMMM YYYY LT',
            l : 'D/M/YYYY',
            ll : 'D MMM YYYY',
            lll : 'D MMM YYYY LT',
            llll : 'ddd, D MMM YYYY LT'
        },
        calendar : {
            sameDay : '[���� �羚]LT',
            nextDay : '[��淚 �羚]LT',
            nextWeek : 'dddd [�漏鬧�] LT',
            lastDay : '[�瘻��� �羚]LT',
            lastWeek : '[����] dddd [���淚�� �漏鬧�] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '�鬧�� %s',
            past : '�壘�� %s',
            s : '�遼壘淚 漏���瘻',
            m : '�樓�',
            mm : '%d �樓�瘻',
            h : '漏鬧�',
            hh : function (number) {
                if (number === 2) {
                    return '漏鬧瘻���';
                }
                return number + ' 漏鬧�瘻';
            },
            d : '���',
            dd : function (number) {
                if (number === 2) {
                    return '������';
                }
                return number + ' ����';
            },
            M : '���漏',
            MM : function (number) {
                if (number === 2) {
                    return '���漏���';
                }
                return number + ' ���漏��';
            },
            y : '漏��',
            yy : function (number) {
                if (number === 2) {
                    return '漏�瘻���';
                } else if (number % 10 === 0 && number !== 10) {
                    return number + ' 漏��';
                }
                return number + ' 漏���';
            }
        }
    });
}));
// moment.js locale configuration
// locale : hindi (hi)
// author : Mayank Singhal : https://github.com/mayanksinghal

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '誓�',
        '2': '誓�',
        '3': '誓�',
        '4': '誓�',
        '5': '誓�',
        '6': '誓�',
        '7': '誓�',
        '8': '誓�',
        '9': '誓�',
        '0': '誓�'
    },
    numberMap = {
        '誓�': '1',
        '誓�': '2',
        '誓�': '3',
        '誓�': '4',
        '誓�': '5',
        '誓�': '6',
        '誓�': '7',
        '誓�': '8',
        '誓�': '9',
        '誓�': '0'
    };

    return moment.defineLocale('hi', {
        months : '西쒉ㄸ西듀ㅀ誓_西ムㅌ西겯ㅅ西겯�_西�ㅎ西겯쪓西�_西끶ㄺ誓띭ㅀ誓댽ㅂ_西�쨮_西쒉쪈西�_西쒉쪇西꿋ㅎ西�_西끶쨽西멘쪓西�_西멘ㅏ西ㅰㄾ誓띭ㄼ西�_西끶쨻誓띭쩅誓귖ㄼ西�_西ⓣㅅ西�쪓西оㅀ_西╆ㅏ西멘ㄾ誓띭ㄼ西�'.split('_'),
        monthsShort : '西쒉ㄸ._西ムㅌ西�._西�ㅎ西겯쪓西�_西끶ㄺ誓띭ㅀ誓�._西�쨮_西쒉쪈西�_西쒉쪇西�._西끶쨽._西멘ㅏ西�._西끶쨻誓띭쩅誓�._西ⓣㅅ._西╆ㅏ西�.'.split('_'),
        weekdays : '西겯ㅅ西욈ㅅ西약ㅀ_西멘쪑西�ㅅ西약ㅀ_西�쨧西쀠ㅂ西듀ㅎ西�_西о쪇西㏅ㅅ西약ㅀ_西쀠쪇西겯쪈西듀ㅎ西�_西뜩쪇西뺖쪓西겯ㅅ西약ㅀ_西뜩ㄸ西욈ㅅ西약ㅀ'.split('_'),
        weekdaysShort : '西겯ㅅ西�_西멘쪑西�_西�쨧西쀠ㅂ_西о쪇西�_西쀠쪇西겯쪈_西뜩쪇西뺖쪓西�_西뜩ㄸ西�'.split('_'),
        weekdaysMin : '西�_西멘쪑_西�쨧_西о쪇_西쀠쪇_西뜩쪇_西�'.split('_'),
        longDateFormat : {
            LT : 'A h:mm 西о쩂誓�',
            LTS : 'A h:mm:ss 西о쩂誓�',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY, LT',
            LLLL : 'dddd, D MMMM YYYY, LT'
        },
        calendar : {
            sameDay : '[西녱쩂] LT',
            nextDay : '[西뺖ㅂ] LT',
            nextWeek : 'dddd, LT',
            lastDay : '[西뺖ㅂ] LT',
            lastWeek : '[西むㅏ西쎹ㅂ誓�] dddd, LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s 西�쪍西�',
            past : '%s 西むㅉ西꿋쪍',
            s : '西뺖쪇西� 西밝� 西뺖쪓西룅ㄳ',
            m : '西뤲쨻 西�ㅏ西ⓣ쩅',
            mm : '%d 西�ㅏ西ⓣ쩅',
            h : '西뤲쨻 西섁쨧西잀ㅎ',
            hh : '%d 西섁쨧西잀쪍',
            d : '西뤲쨻 西╆ㅏ西�',
            dd : '%d 西╆ㅏ西�',
            M : '西뤲쨻 西�ㅉ誓西ⓣ쪍',
            MM : '%d 西�ㅉ誓西ⓣ쪍',
            y : '西뤲쨻 西듀ㅀ誓띭ㅇ',
            yy : '%d 西듀ㅀ誓띭ㅇ'
        },
        preparse: function (string) {
            return string.replace(/[誓㏅ⅷ誓⒯ⅹ誓ム�誓��誓�ⅵ]/g, function (match) {
                return numberMap[match];
            });
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            });
        },
        // Hindi notation for meridiems are quite fuzzy in practice. While there exists
        // a rigid notion of a 'Pahar' it is not used as rigidly in modern Hindi.
        meridiemParse: /西겯ㅎ西�|西멘쪇西оㅉ|西╆쪑西むㅉ西�|西뜩ㅎ西�/,
        meridiemHour : function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === '西겯ㅎ西�') {
                return hour < 4 ? hour : hour + 12;
            } else if (meridiem === '西멘쪇西оㅉ') {
                return hour;
            } else if (meridiem === '西╆쪑西むㅉ西�') {
                return hour >= 10 ? hour : hour + 12;
            } else if (meridiem === '西뜩ㅎ西�') {
                return hour + 12;
            }
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 4) {
                return '西겯ㅎ西�';
            } else if (hour < 10) {
                return '西멘쪇西оㅉ';
            } else if (hour < 17) {
                return '西╆쪑西むㅉ西�';
            } else if (hour < 20) {
                return '西뜩ㅎ西�';
            } else {
                return '西겯ㅎ西�';
            }
        },
        week : {
            dow : 0, // Sunday is the first day of the week.
            doy : 6  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : hrvatski (hr)
// author : Bojan Markovi훶 : https://github.com/bmarkovic

// based on (sl) translation by Robert Sedov큄ek

(function (factory) {
    factory(moment);
}(function (moment) {
    function translate(number, withoutSuffix, key) {
        var result = number + ' ';
        switch (key) {
        case 'm':
            return withoutSuffix ? 'jedna minuta' : 'jedne minute';
        case 'mm':
            if (number === 1) {
                result += 'minuta';
            } else if (number === 2 || number === 3 || number === 4) {
                result += 'minute';
            } else {
                result += 'minuta';
            }
            return result;
        case 'h':
            return withoutSuffix ? 'jedan sat' : 'jednog sata';
        case 'hh':
            if (number === 1) {
                result += 'sat';
            } else if (number === 2 || number === 3 || number === 4) {
                result += 'sata';
            } else {
                result += 'sati';
            }
            return result;
        case 'dd':
            if (number === 1) {
                result += 'dan';
            } else {
                result += 'dana';
            }
            return result;
        case 'MM':
            if (number === 1) {
                result += 'mjesec';
            } else if (number === 2 || number === 3 || number === 4) {
                result += 'mjeseca';
            } else {
                result += 'mjeseci';
            }
            return result;
        case 'yy':
            if (number === 1) {
                result += 'godina';
            } else if (number === 2 || number === 3 || number === 4) {
                result += 'godine';
            } else {
                result += 'godina';
            }
            return result;
        }
    }

    return moment.defineLocale('hr', {
        months : 'sje훾anj_velja훾a_o탑ujak_travanj_svibanj_lipanj_srpanj_kolovoz_rujan_listopad_studeni_prosinac'.split('_'),
        monthsShort : 'sje._vel._o탑u._tra._svi._lip._srp._kol._ruj._lis._stu._pro.'.split('_'),
        weekdays : 'nedjelja_ponedjeljak_utorak_srijeda_훾etvrtak_petak_subota'.split('_'),
        weekdaysShort : 'ned._pon._uto._sri._훾et._pet._sub.'.split('_'),
        weekdaysMin : 'ne_po_ut_sr_훾e_pe_su'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'DD. MM. YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY LT',
            LLLL : 'dddd, D. MMMM YYYY LT'
        },
        calendar : {
            sameDay  : '[danas u] LT',
            nextDay  : '[sutra u] LT',

            nextWeek : function () {
                switch (this.day()) {
                case 0:
                    return '[u] [nedjelju] [u] LT';
                case 3:
                    return '[u] [srijedu] [u] LT';
                case 6:
                    return '[u] [subotu] [u] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[u] dddd [u] LT';
                }
            },
            lastDay  : '[ju훾er u] LT',
            lastWeek : function () {
                switch (this.day()) {
                case 0:
                case 3:
                    return '[pro큄lu] dddd [u] LT';
                case 6:
                    return '[pro큄le] [subote] [u] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[pro큄li] dddd [u] LT';
                }
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : 'za %s',
            past   : 'prije %s',
            s      : 'par sekundi',
            m      : translate,
            mm     : translate,
            h      : translate,
            hh     : translate,
            d      : 'dan',
            dd     : translate,
            M      : 'mjesec',
            MM     : translate,
            y      : 'godinu',
            yy     : translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : hungarian (hu)
// author : Adam Brunner : https://github.com/adambrunner

(function (factory) {
    factory(moment);
}(function (moment) {
    var weekEndings = 'vas찼rnap h챕tf흷n kedden szerd찼n cs체t철rt철k철n p챕nteken szombaton'.split(' ');

    function translate(number, withoutSuffix, key, isFuture) {
        var num = number,
            suffix;

        switch (key) {
        case 's':
            return (isFuture || withoutSuffix) ? 'n챕h찼ny m찼sodperc' : 'n챕h찼ny m찼sodperce';
        case 'm':
            return 'egy' + (isFuture || withoutSuffix ? ' perc' : ' perce');
        case 'mm':
            return num + (isFuture || withoutSuffix ? ' perc' : ' perce');
        case 'h':
            return 'egy' + (isFuture || withoutSuffix ? ' 처ra' : ' 처r찼ja');
        case 'hh':
            return num + (isFuture || withoutSuffix ? ' 처ra' : ' 처r찼ja');
        case 'd':
            return 'egy' + (isFuture || withoutSuffix ? ' nap' : ' napja');
        case 'dd':
            return num + (isFuture || withoutSuffix ? ' nap' : ' napja');
        case 'M':
            return 'egy' + (isFuture || withoutSuffix ? ' h처nap' : ' h처napja');
        case 'MM':
            return num + (isFuture || withoutSuffix ? ' h처nap' : ' h처napja');
        case 'y':
            return 'egy' + (isFuture || withoutSuffix ? ' 챕v' : ' 챕ve');
        case 'yy':
            return num + (isFuture || withoutSuffix ? ' 챕v' : ' 챕ve');
        }

        return '';
    }

    function week(isFuture) {
        return (isFuture ? '' : '[m첬lt] ') + '[' + weekEndings[this.day()] + '] LT[-kor]';
    }

    return moment.defineLocale('hu', {
        months : 'janu찼r_febru찼r_m찼rcius_찼prilis_m찼jus_j첬nius_j첬lius_augusztus_szeptember_okt처ber_november_december'.split('_'),
        monthsShort : 'jan_feb_m찼rc_찼pr_m찼j_j첬n_j첬l_aug_szept_okt_nov_dec'.split('_'),
        weekdays : 'vas찼rnap_h챕tf흷_kedd_szerda_cs체t철rt철k_p챕ntek_szombat'.split('_'),
        weekdaysShort : 'vas_h챕t_kedd_sze_cs체t_p챕n_szo'.split('_'),
        weekdaysMin : 'v_h_k_sze_cs_p_szo'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'YYYY.MM.DD.',
            LL : 'YYYY. MMMM D.',
            LLL : 'YYYY. MMMM D., LT',
            LLLL : 'YYYY. MMMM D., dddd LT'
        },
        meridiemParse: /de|du/i,
        isPM: function (input) {
            return input.charAt(1).toLowerCase() === 'u';
        },
        meridiem : function (hours, minutes, isLower) {
            if (hours < 12) {
                return isLower === true ? 'de' : 'DE';
            } else {
                return isLower === true ? 'du' : 'DU';
            }
        },
        calendar : {
            sameDay : '[ma] LT[-kor]',
            nextDay : '[holnap] LT[-kor]',
            nextWeek : function () {
                return week.call(this, true);
            },
            lastDay : '[tegnap] LT[-kor]',
            lastWeek : function () {
                return week.call(this, false);
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s m첬lva',
            past : '%s',
            s : translate,
            m : translate,
            mm : translate,
            h : translate,
            hh : translate,
            d : translate,
            dd : translate,
            M : translate,
            MM : translate,
            y : translate,
            yy : translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Armenian (hy-am)
// author : Armendarabyan : https://github.com/armendarabyan

(function (factory) {
    factory(moment);
}(function (moment) {
    function monthsCaseReplace(m, format) {
        var months = {
            'nominative': '卵辣�鸞籃蘿�_�樂纜�籃蘿�_爛蘿�纜_蘿擥�諾酪_爛蘿蘭諾濫_卵辣�鸞諾濫_卵辣�酪諾濫_�裸辣濫纜辣濫_濫樂擥纜樂爛螺樂�_卵辣亂纜樂爛螺樂�_鸞辣蘭樂爛螺樂�_邏樂亂纜樂爛螺樂�'.split('_'),
            'accusative': '卵辣�鸞籃蘿�諾_�樂纜�籃蘿�諾_爛蘿�纜諾_蘿擥�諾酪諾_爛蘿蘭諾濫諾_卵辣�鸞諾濫諾_卵辣�酪諾濫諾_�裸辣濫纜辣濫諾_濫樂擥纜樂爛螺樂�諾_卵辣亂纜樂爛螺樂�諾_鸞辣蘭樂爛螺樂�諾_邏樂亂纜樂爛螺樂�諾'.split('_')
        },

        nounCase = (/D[oD]?(\[[^\[\]]*\]|\s+)+MMMM?/).test(format) ?
            'accusative' :
            'nominative';

        return months[nounCase][m.month()];
    }

    function monthsShortCaseReplace(m, format) {
        var monthsShort = '卵鸞籃_�纜�_爛�纜_蘿擥�_爛蘭濫_卵鸞濫_卵酪濫_�裸濫_濫擥纜_卵亂纜_鸞爛螺_邏亂纜'.split('_');

        return monthsShort[m.month()];
    }

    function weekdaysCaseReplace(m, format) {
        var weekdays = '亂諾�蘿亂諾_樂�亂辣�剌蘿螺絡諾_樂�樂�剌蘿螺絡諾_嵐辣�樂�剌蘿螺絡諾_卵諾鸞裸剌蘿螺絡諾_辣��螺蘿絡_剌蘿螺蘿絡'.split('_');

        return weekdays[m.day()];
    }

    return moment.defineLocale('hy-am', {
        months : monthsCaseReplace,
        monthsShort : monthsShortCaseReplace,
        weekdays : weekdaysCaseReplace,
        weekdaysShort : '亂�亂_樂�亂_樂��_嵐��_卵鸞裸_辣��螺_剌螺絡'.split('_'),
        weekdaysMin : '亂�亂_樂�亂_樂��_嵐��_卵鸞裸_辣��螺_剌螺絡'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY 絡.',
            LLL : 'D MMMM YYYY 絡., LT',
            LLLL : 'dddd, D MMMM YYYY 絡., LT'
        },
        calendar : {
            sameDay: '[蘿蘭濫��] LT',
            nextDay: '[籃蘿欒珞] LT',
            lastDay: '[樂�樂亂] LT',
            nextWeek: function () {
                return 'dddd [��珞 落蘿爛珞] LT';
            },
            lastWeek: function () {
                return '[蘿鸞�蘿丹] dddd [��珞 落蘿爛珞] LT';
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : '%s 卵樂纜辣',
            past : '%s 蘿欖蘿攬',
            s : '爛諾 �蘿鸞諾 籃蘿蘭�亂蘭蘿鸞',
            m : '�辣擥樂',
            mm : '%d �辣擥樂',
            h : '落蘿爛',
            hh : '%d 落蘿爛',
            d : '��',
            dd : '%d ��',
            M : '蘿爛諾濫',
            MM : '%d 蘿爛諾濫',
            y : '纜蘿�諾',
            yy : '%d 纜蘿�諾'
        },

        meridiemParse: /裸諾剌樂�籃蘿|蘿欖蘿籃辣纜籃蘿|�樂�樂亂籃蘿|樂�樂亂辣蘭蘿鸞/,
        isPM: function (input) {
            return /^(�樂�樂亂籃蘿|樂�樂亂辣蘭蘿鸞)$/.test(input);
        },
        meridiem : function (hour) {
            if (hour < 4) {
                return '裸諾剌樂�籃蘿';
            } else if (hour < 12) {
                return '蘿欖蘿籃辣纜籃蘿';
            } else if (hour < 17) {
                return '�樂�樂亂籃蘿';
            } else {
                return '樂�樂亂辣蘭蘿鸞';
            }
        },

        ordinalParse: /\d{1,2}|\d{1,2}-(諾鸞|�邏)/,
        ordinal: function (number, period) {
            switch (period) {
            case 'DDD':
            case 'w':
            case 'W':
            case 'DDDo':
                if (number === 1) {
                    return number + '-諾鸞';
                }
                return number + '-�邏';
            default:
                return number;
            }
        },

        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Bahasa Indonesia (id)
// author : Mohammad Satrio Utomo : https://github.com/tyok
// reference: http://id.wikisource.org/wiki/Pedoman_Umum_Ejaan_Bahasa_Indonesia_yang_Disempurnakan

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('id', {
        months : 'Januari_Februari_Maret_April_Mei_Juni_Juli_Agustus_September_Oktober_November_Desember'.split('_'),
        monthsShort : 'Jan_Feb_Mar_Apr_Mei_Jun_Jul_Ags_Sep_Okt_Nov_Des'.split('_'),
        weekdays : 'Minggu_Senin_Selasa_Rabu_Kamis_Jumat_Sabtu'.split('_'),
        weekdaysShort : 'Min_Sen_Sel_Rab_Kam_Jum_Sab'.split('_'),
        weekdaysMin : 'Mg_Sn_Sl_Rb_Km_Jm_Sb'.split('_'),
        longDateFormat : {
            LT : 'HH.mm',
            LTS : 'LT.ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY [pukul] LT',
            LLLL : 'dddd, D MMMM YYYY [pukul] LT'
        },
        meridiemParse: /pagi|siang|sore|malam/,
        meridiemHour : function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === 'pagi') {
                return hour;
            } else if (meridiem === 'siang') {
                return hour >= 11 ? hour : hour + 12;
            } else if (meridiem === 'sore' || meridiem === 'malam') {
                return hour + 12;
            }
        },
        meridiem : function (hours, minutes, isLower) {
            if (hours < 11) {
                return 'pagi';
            } else if (hours < 15) {
                return 'siang';
            } else if (hours < 19) {
                return 'sore';
            } else {
                return 'malam';
            }
        },
        calendar : {
            sameDay : '[Hari ini pukul] LT',
            nextDay : '[Besok pukul] LT',
            nextWeek : 'dddd [pukul] LT',
            lastDay : '[Kemarin pukul] LT',
            lastWeek : 'dddd [lalu pukul] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'dalam %s',
            past : '%s yang lalu',
            s : 'beberapa detik',
            m : 'semenit',
            mm : '%d menit',
            h : 'sejam',
            hh : '%d jam',
            d : 'sehari',
            dd : '%d hari',
            M : 'sebulan',
            MM : '%d bulan',
            y : 'setahun',
            yy : '%d tahun'
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : icelandic (is)
// author : Hinrik 횜rn Sigur챨sson : https://github.com/hinrik

(function (factory) {
    factory(moment);
}(function (moment) {
    function plural(n) {
        if (n % 100 === 11) {
            return true;
        } else if (n % 10 === 1) {
            return false;
        }
        return true;
    }

    function translate(number, withoutSuffix, key, isFuture) {
        var result = number + ' ';
        switch (key) {
        case 's':
            return withoutSuffix || isFuture ? 'nokkrar sek첬ndur' : 'nokkrum sek첬ndum';
        case 'm':
            return withoutSuffix ? 'm챠n첬ta' : 'm챠n첬tu';
        case 'mm':
            if (plural(number)) {
                return result + (withoutSuffix || isFuture ? 'm챠n첬tur' : 'm챠n첬tum');
            } else if (withoutSuffix) {
                return result + 'm챠n첬ta';
            }
            return result + 'm챠n첬tu';
        case 'hh':
            if (plural(number)) {
                return result + (withoutSuffix || isFuture ? 'klukkustundir' : 'klukkustundum');
            }
            return result + 'klukkustund';
        case 'd':
            if (withoutSuffix) {
                return 'dagur';
            }
            return isFuture ? 'dag' : 'degi';
        case 'dd':
            if (plural(number)) {
                if (withoutSuffix) {
                    return result + 'dagar';
                }
                return result + (isFuture ? 'daga' : 'd철gum');
            } else if (withoutSuffix) {
                return result + 'dagur';
            }
            return result + (isFuture ? 'dag' : 'degi');
        case 'M':
            if (withoutSuffix) {
                return 'm찼nu챨ur';
            }
            return isFuture ? 'm찼nu챨' : 'm찼nu챨i';
        case 'MM':
            if (plural(number)) {
                if (withoutSuffix) {
                    return result + 'm찼nu챨ir';
                }
                return result + (isFuture ? 'm찼nu챨i' : 'm찼nu챨um');
            } else if (withoutSuffix) {
                return result + 'm찼nu챨ur';
            }
            return result + (isFuture ? 'm찼nu챨' : 'm찼nu챨i');
        case 'y':
            return withoutSuffix || isFuture ? '찼r' : '찼ri';
        case 'yy':
            if (plural(number)) {
                return result + (withoutSuffix || isFuture ? '찼r' : '찼rum');
            }
            return result + (withoutSuffix || isFuture ? '찼r' : '찼ri');
        }
    }

    return moment.defineLocale('is', {
        months : 'jan첬ar_febr첬ar_mars_apr챠l_ma챠_j첬n챠_j첬l챠_찼g첬st_september_okt처ber_n처vember_desember'.split('_'),
        monthsShort : 'jan_feb_mar_apr_ma챠_j첬n_j첬l_찼g첬_sep_okt_n처v_des'.split('_'),
        weekdays : 'sunnudagur_m찼nudagur_첸ri챨judagur_mi챨vikudagur_fimmtudagur_f철studagur_laugardagur'.split('_'),
        weekdaysShort : 'sun_m찼n_첸ri_mi챨_fim_f철s_lau'.split('_'),
        weekdaysMin : 'Su_M찼_횧r_Mi_Fi_F철_La'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY [kl.] LT',
            LLLL : 'dddd, D. MMMM YYYY [kl.] LT'
        },
        calendar : {
            sameDay : '[챠 dag kl.] LT',
            nextDay : '[찼 morgun kl.] LT',
            nextWeek : 'dddd [kl.] LT',
            lastDay : '[챠 g챈r kl.] LT',
            lastWeek : '[s챠챨asta] dddd [kl.] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'eftir %s',
            past : 'fyrir %s s챠챨an',
            s : translate,
            m : translate,
            mm : translate,
            h : 'klukkustund',
            hh : translate,
            d : translate,
            dd : translate,
            M : translate,
            MM : translate,
            y : translate,
            yy : translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : italian (it)
// author : Lorenzo : https://github.com/aliem
// author: Mattia Larentis: https://github.com/nostalgiaz

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('it', {
        months : 'gennaio_febbraio_marzo_aprile_maggio_giugno_luglio_agosto_settembre_ottobre_novembre_dicembre'.split('_'),
        monthsShort : 'gen_feb_mar_apr_mag_giu_lug_ago_set_ott_nov_dic'.split('_'),
        weekdays : 'Domenica_Luned챙_Marted챙_Mercoled챙_Gioved챙_Venerd챙_Sabato'.split('_'),
        weekdaysShort : 'Dom_Lun_Mar_Mer_Gio_Ven_Sab'.split('_'),
        weekdaysMin : 'D_L_Ma_Me_G_V_S'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[Oggi alle] LT',
            nextDay: '[Domani alle] LT',
            nextWeek: 'dddd [alle] LT',
            lastDay: '[Ieri alle] LT',
            lastWeek: function () {
                switch (this.day()) {
                    case 0:
                        return '[la scorsa] dddd [alle] LT';
                    default:
                        return '[lo scorso] dddd [alle] LT';
                }
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : function (s) {
                return ((/^[0-9].+$/).test(s) ? 'tra' : 'in') + ' ' + s;
            },
            past : '%s fa',
            s : 'alcuni secondi',
            m : 'un minuto',
            mm : '%d minuti',
            h : 'un\'ora',
            hh : '%d ore',
            d : 'un giorno',
            dd : '%d giorni',
            M : 'un mese',
            MM : '%d mesi',
            y : 'un anno',
            yy : '%d anni'
        },
        ordinalParse : /\d{1,2}쨘/,
        ordinal: '%d쨘',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : japanese (ja)
// author : LI Long : https://github.com/baryon

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('ja', {
        months : '1��_2��_3��_4��_5��_6��_7��_8��_9��_10��_11��_12��'.split('_'),
        monthsShort : '1��_2��_3��_4��_5��_6��_7��_8��_9��_10��_11��_12��'.split('_'),
        weekdays : '�ζ썫��_�덃썫��_�ユ썫��_麗닸썫��_�ⓩ썫��_�묉썫��_�잍썫��'.split('_'),
        weekdaysShort : '��_��_��_麗�_��_��_��'.split('_'),
        weekdaysMin : '��_��_��_麗�_��_��_��'.split('_'),
        longDateFormat : {
            LT : 'Ah�굆��',
            LTS : 'LTs燁�',
            L : 'YYYY/MM/DD',
            LL : 'YYYY亮퀾�뉲��',
            LLL : 'YYYY亮퀾�뉲�쩖T',
            LLLL : 'YYYY亮퀾�뉲�쩖T dddd'
        },
        meridiemParse: /�덂뎺|�덂풄/i,
        isPM : function (input) {
            return input === '�덂풄';
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 12) {
                return '�덂뎺';
            } else {
                return '�덂풄';
            }
        },
        calendar : {
            sameDay : '[餓딀뿥] LT',
            nextDay : '[�롦뿥] LT',
            nextWeek : '[�ι�]dddd LT',
            lastDay : '[�ⓩ뿥] LT',
            lastWeek : '[�띺�]dddd LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s孃�',
            past : '%s��',
            s : '�곁쭜',
            m : '1��',
            mm : '%d��',
            h : '1�귡뼋',
            hh : '%d�귡뼋',
            d : '1��',
            dd : '%d��',
            M : '1�뜻쐢',
            MM : '%d�뜻쐢',
            y : '1亮�',
            yy : '%d亮�'
        }
    });
}));
// moment.js locale configuration
// locale : Georgian (ka)
// author : Irakli Janiashvili : https://github.com/irakli-janiashvili

(function (factory) {
    factory(moment);
}(function (moment) {
    function monthsCaseReplace(m, format) {
        var months = {
            'nominative': '�섂깘�쒊깢�먤깲��_�쀡깞�묃깞�졹깢�먤깪��_�쎺깘�졹깴��_�먤깯�졹깦�싡깦_�쎺깘�섂깳��_�섂깢�쒊깦�■깦_�섂깢�싡깦�■깦_�먤깚�뺗깦�■깴��_�■깞�α깴�붳깫�묃깞�졹깦_�앩깷�㏇깮�쎺깙�붳깲��_�쒊깮�붳깫�묃깞�졹깦_�볚깞�쇹깞�쎺깙�붳깲��'.split('_'),
            'accusative': '�섂깘�쒊깢�먤깲��_�쀡깞�묃깞�졹깢�먤깪��_�쎺깘�졹깴��_�먤깯�졹깦�싡깦��_�쎺깘�섂깳��_�섂깢�쒊깦�■깳_�섂깢�싡깦�■깳_�먤깚�뺗깦�■깴��_�■깞�α깴�붳깫�묃깞�졹깳_�앩깷�㏇깮�쎺깙�붳깲��_�쒊깮�붳깫�묃깞�졹깳_�볚깞�쇹깞�쎺깙�붳깲��'.split('_')
        },

        nounCase = (/D[oD] *MMMM?/).test(format) ?
            'accusative' :
            'nominative';

        return months[nounCase][m.month()];
    }

    function weekdaysCaseReplace(m, format) {
        var weekdays = {
            'nominative': '�쇹깢�섂깲��_�앩깲�ⓤ깘�묃깘�쀡깦_�■깘�쎺깿�먤깙�먤깤��_�앩깤��깿�먤깙�먤깤��_��깵�쀡깿�먤깙�먤깤��_�왾깘�졹깘�■깧�붳깢��_�ⓤ깘�묃깘�쀡깦'.split('_'),
            'accusative': '�쇹깢�섂깲�먤깳_�앩깲�ⓤ깘�묃깘�쀡깳_�■깘�쎺깿�먤깙�먤깤��_�앩깤��깿�먤깙�먤깤��_��깵�쀡깿�먤깙�먤깤��_�왾깘�졹깘�■깧�붳깢��_�ⓤ깘�묃깘�쀡깳'.split('_')
        },

        nounCase = (/(�п깦�쒊깘|�ⓤ깞�쎺깛�붳깚)/).test(format) ?
            'accusative' :
            'nominative';

        return weekdays[nounCase][m.day()];
    }

    return moment.defineLocale('ka', {
        months : monthsCaseReplace,
        monthsShort : '�섂깘��_�쀡깞��_�쎺깘��_�먤깯��_�쎺깘��_�섂깢��_�섂깢��_�먤깚��_�■깞��_�앩깷��_�쒊깮��_�볚깞��'.split('_'),
        weekdays : weekdaysCaseReplace,
        weekdaysShort : '�쇹깢��_�앩깲��_�■깘��_�앩깤��_��깵��_�왾깘��_�ⓤ깘��'.split('_'),
        weekdaysMin : '�쇹깢_�앩깲_�■깘_�앩깤_��깵_�왾깘_�ⓤ깘'.split('_'),
        longDateFormat : {
            LT : 'h:mm A',
            LTS : 'h:mm:ss A',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[�볚깺�붳깳] LT[-�뽥깞]',
            nextDay : '[��깢�먤깪] LT[-�뽥깞]',
            lastDay : '[�믟깵�ⓤ깦��] LT[-�뽥깞]',
            nextWeek : '[�ⓤ깞�쎺깛�붳깚] dddd LT[-�뽥깞]',
            lastWeek : '[�п깦�쒊깘] dddd LT-�뽥깞',
            sameElse : 'L'
        },
        relativeTime : {
            future : function (s) {
                return (/(�п깘�쎺깦|�п깵�쀡깦|�■깘�먤깤��|�п깞�싡깦)/).test(s) ?
                    s.replace(/��$/, '�ⓤ깦') :
                    s + '�ⓤ깦';
            },
            past : function (s) {
                if ((/(�п깘�쎺깦|�п깵�쀡깦|�■깘�먤깤��|�볚깺��|�쀡깢��)/).test(s)) {
                    return s.replace(/(��|��)$/, '�섂깳 �п깦��');
                }
                if ((/�п깞�싡깦/).test(s)) {
                    return s.replace(/�п깞�싡깦$/, '�п깪�섂깳 �п깦��');
                }
            },
            s : '�졹깘�쎺깛�붳깭�섂깫�� �п깘�쎺깦',
            m : '�п깵�쀡깦',
            mm : '%d �п깵�쀡깦',
            h : '�■깘�먤깤��',
            hh : '%d �■깘�먤깤��',
            d : '�볚깺��',
            dd : '%d �볚깺��',
            M : '�쀡깢��',
            MM : '%d �쀡깢��',
            y : '�п깞�싡깦',
            yy : '%d �п깞�싡깦'
        },
        ordinalParse: /0|1-�싡깦|�쎺깞-\d{1,2}|\d{1,2}-��/,
        ordinal : function (number) {
            if (number === 0) {
                return number;
            }

            if (number === 1) {
                return number + '-�싡깦';
            }

            if ((number < 20) || (number <= 100 && (number % 20 === 0)) || (number % 100 === 0)) {
                return '�쎺깞-' + number;
            }

            return number + '-��';
        },
        week : {
            dow : 1,
            doy : 7
        }
    });
}));
// moment.js locale configuration
// locale : khmer (km)
// author : Kruy Vanna : https://github.com/kruyvanna

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('km', {
        months: '�섂��싡왃_��삔옒�믟옑��_�섂왅�볚왃_�섂웳�잁왃_�㎭옝�쀡왃_�섂왅�먤왊�볚왃_���믟��듻왃_�잁왆�졹왃_��됣윊�됣왃_�뤳왊�쎺왃_�쒊왅�끷윊�녲왅���_�믟윊�볚왋'.split('_'),
        monthsShort: '�섂��싡왃_��삔옒�믟옑��_�섂왅�볚왃_�섂웳�잁왃_�㎭옝�쀡왃_�섂왅�먤왊�볚왃_���믟��듻왃_�잁왆�졹왃_��됣윊�됣왃_�뤳왊�쎺왃_�쒊왅�끷윊�녲왅���_�믟윊�볚왋'.split('_'),
        weekdays: '�㏇왃�묃왅�뤳윊��_�끷윇�볚윊��_�㏇엫�믟엩�뜬옔_�뽥왊��_�뽥윊�싡옞�잁윊�붳옃�료윂_�잁왊��믟옔_�잁웷�싡윂'.split('_'),
        weekdaysShort: '�㏇왃�묃왅�뤳윊��_�끷윇�볚윊��_�㏇엫�믟엩�뜬옔_�뽥왊��_�뽥윊�싡옞�잁윊�붳옃�료윂_�잁왊��믟옔_�잁웷�싡윂'.split('_'),
        weekdaysMin: '�㏇왃�묃왅�뤳윊��_�끷윇�볚윊��_�㏇엫�믟엩�뜬옔_�뽥왊��_�뽥윊�싡옞�잁윊�붳옃�료윂_�잁왊��믟옔_�잁웷�싡윂'.split('_'),
        longDateFormat: {
            LT: 'HH:mm',
            LTS : 'LT:ss',
            L: 'DD/MM/YYYY',
            LL: 'D MMMM YYYY',
            LLL: 'D MMMM YYYY LT',
            LLLL: 'dddd, D MMMM YYYY LT'
        },
        calendar: {
            sameDay: '[�먤윊�꾞웵�볚웼 �섂웾�꾞엫] LT',
            nextDay: '[�잁윊�㏇웴� �섂웾�꾞엫] LT',
            nextWeek: 'dddd [�섂웾�꾞엫] LT',
            lastDay: '[�섂윊�잁왅�쎺옒�료엵 �섂웾�꾞엫] LT',
            lastWeek: 'dddd [�잁옍�믟옃�뜬옞�띮옒�삔옋] [�섂웾�꾞엫] LT',
            sameElse: 'L'
        },
        relativeTime: {
            future: '%s�묃���',
            past: '%s�섂왊��',
            s: '�붳웾�삔옋�믟옒�뜬옋�쒊왅�볚왃�묃왆',
            m: '�섂왌�쇹옋�뜬옉��',
            mm: '%d �볚왃�묃왆',
            h: '�섂왌�쇹옒�됣웶��',
            hh: '%d �섂웾�꾞엫',
            d: '�섂왌�쇹옄�믟엫��',
            dd: '%d �먤윊�꾞웵',
            M: '�섂왌�쇹엨��',
            MM: '%d �곢웴',
            y: '�섂왌�쇹엱�믟옋�뜬웺',
            yy: '%d �녲윊�볚왃��'
        },
        week: {
            dow: 1, // Monday is the first day of the week.
            doy: 4 // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : korean (ko)
//
// authors
//
// - Kyungwook, Park : https://github.com/kyungw00k
// - Jeeeyul Lee <jeeeyul@gmail.com>
(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('ko', {
        months : '1��_2��_3��_4��_5��_6��_7��_8��_9��_10��_11��_12��'.split('_'),
        monthsShort : '1��_2��_3��_4��_5��_6��_7��_8��_9��_10��_11��_12��'.split('_'),
        weekdays : '�쇱슂��_�붿슂��_�붿슂��_�섏슂��_紐⑹슂��_湲덉슂��_�좎슂��'.split('_'),
        weekdaysShort : '��_��_��_��_紐�_湲�_��'.split('_'),
        weekdaysMin : '��_��_��_��_紐�_湲�_��'.split('_'),
        longDateFormat : {
            LT : 'A h�� m遺�',
            LTS : 'A h�� m遺� s珥�',
            L : 'YYYY.MM.DD',
            LL : 'YYYY�� MMMM D��',
            LLL : 'YYYY�� MMMM D�� LT',
            LLLL : 'YYYY�� MMMM D�� dddd LT'
        },
        calendar : {
            sameDay : '�ㅻ뒛 LT',
            nextDay : '�댁씪 LT',
            nextWeek : 'dddd LT',
            lastDay : '�댁젣 LT',
            lastWeek : '吏�쒖＜ dddd LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s ��',
            past : '%s ��',
            s : '紐뉗큹',
            ss : '%d珥�',
            m : '�쇰텇',
            mm : '%d遺�',
            h : '�쒖떆媛�',
            hh : '%d�쒓컙',
            d : '�섎（',
            dd : '%d��',
            M : '�쒕떖',
            MM : '%d��',
            y : '�쇰뀈',
            yy : '%d��'
        },
        ordinalParse : /\d{1,2}��/,
        ordinal : '%d��',
        meridiemParse : /�ㅼ쟾|�ㅽ썑/,
        isPM : function (token) {
            return token === '�ㅽ썑';
        },
        meridiem : function (hour, minute, isUpper) {
            return hour < 12 ? '�ㅼ쟾' : '�ㅽ썑';
        }
    });
}));
// moment.js locale configuration
// locale : Luxembourgish (lb)
// author : mweimerskirch : https://github.com/mweimerskirch, David Raison : https://github.com/kwisatz

// Note: Luxembourgish has a very particular phonological rule ('Eifeler Regel') that causes the
// deletion of the final 'n' in certain contexts. That's what the 'eifelerRegelAppliesToWeekday'
// and 'eifelerRegelAppliesToNumber' methods are meant for

(function (factory) {
    factory(moment);
}(function (moment) {
    function processRelativeTime(number, withoutSuffix, key, isFuture) {
        var format = {
            'm': ['eng Minutt', 'enger Minutt'],
            'h': ['eng Stonn', 'enger Stonn'],
            'd': ['een Dag', 'engem Dag'],
            'M': ['ee Mount', 'engem Mount'],
            'y': ['ee Joer', 'engem Joer']
        };
        return withoutSuffix ? format[key][0] : format[key][1];
    }

    function processFutureTime(string) {
        var number = string.substr(0, string.indexOf(' '));
        if (eifelerRegelAppliesToNumber(number)) {
            return 'a ' + string;
        }
        return 'an ' + string;
    }

    function processPastTime(string) {
        var number = string.substr(0, string.indexOf(' '));
        if (eifelerRegelAppliesToNumber(number)) {
            return 'viru ' + string;
        }
        return 'virun ' + string;
    }

    /**
     * Returns true if the word before the given number loses the '-n' ending.
     * e.g. 'an 10 Deeg' but 'a 5 Deeg'
     *
     * @param number {integer}
     * @returns {boolean}
     */
    function eifelerRegelAppliesToNumber(number) {
        number = parseInt(number, 10);
        if (isNaN(number)) {
            return false;
        }
        if (number < 0) {
            // Negative Number --> always true
            return true;
        } else if (number < 10) {
            // Only 1 digit
            if (4 <= number && number <= 7) {
                return true;
            }
            return false;
        } else if (number < 100) {
            // 2 digits
            var lastDigit = number % 10, firstDigit = number / 10;
            if (lastDigit === 0) {
                return eifelerRegelAppliesToNumber(firstDigit);
            }
            return eifelerRegelAppliesToNumber(lastDigit);
        } else if (number < 10000) {
            // 3 or 4 digits --> recursively check first digit
            while (number >= 10) {
                number = number / 10;
            }
            return eifelerRegelAppliesToNumber(number);
        } else {
            // Anything larger than 4 digits: recursively check first n-3 digits
            number = number / 1000;
            return eifelerRegelAppliesToNumber(number);
        }
    }

    return moment.defineLocale('lb', {
        months: 'Januar_Februar_M채erz_Abr챘ll_Mee_Juni_Juli_August_September_Oktober_November_Dezember'.split('_'),
        monthsShort: 'Jan._Febr._Mrz._Abr._Mee_Jun._Jul._Aug._Sept._Okt._Nov._Dez.'.split('_'),
        weekdays: 'Sonndeg_M챕indeg_D챘nschdeg_M챘ttwoch_Donneschdeg_Freideg_Samschdeg'.split('_'),
        weekdaysShort: 'So._M챕._D챘._M챘._Do._Fr._Sa.'.split('_'),
        weekdaysMin: 'So_M챕_D챘_M챘_Do_Fr_Sa'.split('_'),
        longDateFormat: {
            LT: 'H:mm [Auer]',
            LTS: 'H:mm:ss [Auer]',
            L: 'DD.MM.YYYY',
            LL: 'D. MMMM YYYY',
            LLL: 'D. MMMM YYYY LT',
            LLLL: 'dddd, D. MMMM YYYY LT'
        },
        calendar: {
            sameDay: '[Haut um] LT',
            sameElse: 'L',
            nextDay: '[Muer um] LT',
            nextWeek: 'dddd [um] LT',
            lastDay: '[G챘schter um] LT',
            lastWeek: function () {
                // Different date string for 'D챘nschdeg' (Tuesday) and 'Donneschdeg' (Thursday) due to phonological rule
                switch (this.day()) {
                    case 2:
                    case 4:
                        return '[Leschten] dddd [um] LT';
                    default:
                        return '[Leschte] dddd [um] LT';
                }
            }
        },
        relativeTime : {
            future : processFutureTime,
            past : processPastTime,
            s : 'e puer Sekonnen',
            m : processRelativeTime,
            mm : '%d Minutten',
            h : processRelativeTime,
            hh : '%d Stonnen',
            d : processRelativeTime,
            dd : '%d Deeg',
            M : processRelativeTime,
            MM : '%d M챕int',
            y : processRelativeTime,
            yy : '%d Joer'
        },
        ordinalParse: /\d{1,2}\./,
        ordinal: '%d.',
        week: {
            dow: 1, // Monday is the first day of the week.
            doy: 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Lithuanian (lt)
// author : Mindaugas Moz큰ras : https://github.com/mmozuras

(function (factory) {
    factory(moment);
}(function (moment) {
    var units = {
        'm' : 'minut휊_minut휊s_minut휌',
        'mm': 'minut휊s_minu훾i킬_minutes',
        'h' : 'valanda_valandos_valand훳',
        'hh': 'valandos_valand킬_valandas',
        'd' : 'diena_dienos_dien훳',
        'dd': 'dienos_dien킬_dienas',
        'M' : 'm휊nuo_m휊nesio_m휊nes캄',
        'MM': 'm휊nesiai_m휊nesi킬_m휊nesius',
        'y' : 'metai_met킬_metus',
        'yy': 'metai_met킬_metus'
    },
    weekDays = 'sekmadienis_pirmadienis_antradienis_tre훾iadienis_ketvirtadienis_penktadienis_큄e큄tadienis'.split('_');

    function translateSeconds(number, withoutSuffix, key, isFuture) {
        if (withoutSuffix) {
            return 'kelios sekund휊s';
        } else {
            return isFuture ? 'keli킬 sekund탑i킬' : 'kelias sekundes';
        }
    }

    function translateSingular(number, withoutSuffix, key, isFuture) {
        return withoutSuffix ? forms(key)[0] : (isFuture ? forms(key)[1] : forms(key)[2]);
    }

    function special(number) {
        return number % 10 === 0 || (number > 10 && number < 20);
    }

    function forms(key) {
        return units[key].split('_');
    }

    function translate(number, withoutSuffix, key, isFuture) {
        var result = number + ' ';
        if (number === 1) {
            return result + translateSingular(number, withoutSuffix, key[0], isFuture);
        } else if (withoutSuffix) {
            return result + (special(number) ? forms(key)[1] : forms(key)[0]);
        } else {
            if (isFuture) {
                return result + forms(key)[1];
            } else {
                return result + (special(number) ? forms(key)[1] : forms(key)[2]);
            }
        }
    }

    function relativeWeekDay(moment, format) {
        var nominative = format.indexOf('dddd HH:mm') === -1,
            weekDay = weekDays[moment.day()];

        return nominative ? weekDay : weekDay.substring(0, weekDay.length - 2) + '캄';
    }

    return moment.defineLocale('lt', {
        months : 'sausio_vasario_kovo_baland탑io_gegu탑휊s_bir탑elio_liepos_rugpj큰훾io_rugs휊jo_spalio_lapkri훾io_gruod탑io'.split('_'),
        monthsShort : 'sau_vas_kov_bal_geg_bir_lie_rgp_rgs_spa_lap_grd'.split('_'),
        weekdays : relativeWeekDay,
        weekdaysShort : 'Sek_Pir_Ant_Tre_Ket_Pen_힋e큄'.split('_'),
        weekdaysMin : 'S_P_A_T_K_Pn_힋'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'YYYY-MM-DD',
            LL : 'YYYY [m.] MMMM D [d.]',
            LLL : 'YYYY [m.] MMMM D [d.], LT [val.]',
            LLLL : 'YYYY [m.] MMMM D [d.], dddd, LT [val.]',
            l : 'YYYY-MM-DD',
            ll : 'YYYY [m.] MMMM D [d.]',
            lll : 'YYYY [m.] MMMM D [d.], LT [val.]',
            llll : 'YYYY [m.] MMMM D [d.], ddd, LT [val.]'
        },
        calendar : {
            sameDay : '[힋iandien] LT',
            nextDay : '[Rytoj] LT',
            nextWeek : 'dddd LT',
            lastDay : '[Vakar] LT',
            lastWeek : '[Pra휊jus캄] dddd LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'po %s',
            past : 'prie큄 %s',
            s : translateSeconds,
            m : translateSingular,
            mm : translate,
            h : translateSingular,
            hh : translate,
            d : translateSingular,
            dd : translate,
            M : translateSingular,
            MM : translate,
            y : translateSingular,
            yy : translate
        },
        ordinalParse: /\d{1,2}-oji/,
        ordinal : function (number) {
            return number + '-oji';
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : latvian (lv)
// author : Kristaps Karlsons : https://github.com/skakri

(function (factory) {
    factory(moment);
}(function (moment) {
    var units = {
        'mm': 'min큰ti_min큰tes_min큰te_min큰tes',
        'hh': 'stundu_stundas_stunda_stundas',
        'dd': 'dienu_dienas_diena_dienas',
        'MM': 'm휆nesi_m휆ne큄us_m휆nesis_m휆ne큄i',
        'yy': 'gadu_gadus_gads_gadi'
    };

    function format(word, number, withoutSuffix) {
        var forms = word.split('_');
        if (withoutSuffix) {
            return number % 10 === 1 && number !== 11 ? forms[2] : forms[3];
        } else {
            return number % 10 === 1 && number !== 11 ? forms[0] : forms[1];
        }
    }

    function relativeTimeWithPlural(number, withoutSuffix, key) {
        return number + ' ' + format(units[key], number, withoutSuffix);
    }

    return moment.defineLocale('lv', {
        months : 'janv훮ris_febru훮ris_marts_apr카lis_maijs_j큰nijs_j큰lijs_augusts_septembris_oktobris_novembris_decembris'.split('_'),
        monthsShort : 'jan_feb_mar_apr_mai_j큰n_j큰l_aug_sep_okt_nov_dec'.split('_'),
        weekdays : 'sv휆tdiena_pirmdiena_otrdiena_tre큄diena_ceturtdiena_piektdiena_sestdiena'.split('_'),
        weekdaysShort : 'Sv_P_O_T_C_Pk_S'.split('_'),
        weekdaysMin : 'Sv_P_O_T_C_Pk_S'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'YYYY. [gada] D. MMMM',
            LLL : 'YYYY. [gada] D. MMMM, LT',
            LLLL : 'YYYY. [gada] D. MMMM, dddd, LT'
        },
        calendar : {
            sameDay : '[힋odien pulksten] LT',
            nextDay : '[R카t pulksten] LT',
            nextWeek : 'dddd [pulksten] LT',
            lastDay : '[Vakar pulksten] LT',
            lastWeek : '[Pag훮ju큄훮] dddd [pulksten] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s v휆l훮k',
            past : '%s agr훮k',
            s : 'da탑as sekundes',
            m : 'min큰ti',
            mm : relativeTimeWithPlural,
            h : 'stundu',
            hh : relativeTimeWithPlural,
            d : 'dienu',
            dd : relativeTimeWithPlural,
            M : 'm휆nesi',
            MM : relativeTimeWithPlural,
            y : 'gadu',
            yy : relativeTimeWithPlural
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : macedonian (mk)
// author : Borislav Mickov : https://github.com/B0k0

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('mk', {
        months : '�逵戟�逵�龜_�筠勻��逵�龜_劇逵��_逵極�龜剋_劇逵�_��戟龜_��剋龜_逵勻均���_�筠極�筠劇勻�龜_棘克�棘劇勻�龜_戟棘筠劇勻�龜_畇筠克筠劇勻�龜'.split('_'),
        monthsShort : '�逵戟_�筠勻_劇逵�_逵極�_劇逵�_��戟_��剋_逵勻均_�筠極_棘克�_戟棘筠_畇筠克'.split('_'),
        weekdays : '戟筠畇筠剋逵_極棘戟筠畇筠剋戟龜克_勻�棘�戟龜克_��筠畇逵_�筠�勻��棘克_極筠�棘克_�逵閨棘�逵'.split('_'),
        weekdaysShort : '戟筠畇_極棘戟_勻�棘_��筠_�筠�_極筠�_�逵閨'.split('_'),
        weekdaysMin : '戟e_極o_勻�_��_�筠_極筠_�a'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'D.MM.YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[�筠戟筠� 勻棘] LT',
            nextDay : '[叫��筠 勻棘] LT',
            nextWeek : 'dddd [勻棘] LT',
            lastDay : '[��筠�逵 勻棘] LT',
            lastWeek : function () {
                switch (this.day()) {
                case 0:
                case 3:
                case 6:
                    return '[�棘 龜鈞劇龜戟逵�逵�逵] dddd [勻棘] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[�棘 龜鈞劇龜戟逵�龜棘�] dddd [勻棘] LT';
                }
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : '極棘�剋筠 %s',
            past : '極�筠畇 %s',
            s : '戟筠克棘剋克� �筠克�戟畇龜',
            m : '劇龜戟��逵',
            mm : '%d 劇龜戟��龜',
            h : '�逵�',
            hh : '%d �逵�逵',
            d : '畇筠戟',
            dd : '%d 畇筠戟逵',
            M : '劇筠�筠�',
            MM : '%d 劇筠�筠�龜',
            y : '均棘畇龜戟逵',
            yy : '%d 均棘畇龜戟龜'
        },
        ordinalParse: /\d{1,2}-(筠勻|筠戟|�龜|勻龜|�龜|劇龜)/,
        ordinal : function (number) {
            var lastDigit = number % 10,
                last2Digits = number % 100;
            if (number === 0) {
                return number + '-筠勻';
            } else if (last2Digits === 0) {
                return number + '-筠戟';
            } else if (last2Digits > 10 && last2Digits < 20) {
                return number + '-�龜';
            } else if (lastDigit === 1) {
                return number + '-勻龜';
            } else if (lastDigit === 2) {
                return number + '-�龜';
            } else if (lastDigit === 7 || lastDigit === 8) {
                return number + '-劇龜';
            } else {
                return number + '-�龜';
            }
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : malayalam (ml)
// author : Floyd Pink : https://github.com/floydpink

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('ml', {
        months : '石쒉뇽碩곟뉘石겯늉_石ム탣石о탫石겯탛石듀눗石�_石�늅碩솰킎碩띭킎碩�_石뤲눅碩띭눗石욈돕_石�탥石�탫_石쒉탞碩�_石쒉탞石꿋탦_石볙킋石멘탫石긍탫石긍탫_石멘탣石む탫石긍탫石긍큲石о돔_石믞킉碩띭킓碩뗠눋碩�_石ⓣ뉘石귖눋碩�_石□늉石멘큲石о돔'.split('_'),
        monthsShort : '石쒉뇽碩�._石ム탣石о탫石겯탛._石�늅碩�._石뤲눅碩띭눗石�._石�탥石�탫_石쒉탞碩�_石쒉탞石꿋탦._石볙킋._石멘탣石む탫石긍탫石�._石믞킉碩띭킓碩�._石ⓣ뉘石�._石□늉石멘큲.'.split('_'),
        weekdays : '石왽늅石�눙石약눼碩띭킎_石ㅰ늉石쇸탫石뺖눴石약눼碩띭킎_石싟탨石듀탫石듀늅石닮탫石�_石о탛石㏅뇽石약눼碩띭킎_石듀탫石�늅石닮늅石닮탫石�_石듀탣石녀탫石녀늉石�늅石닮탫石�_石뜩뇽石욈눕石약눼碩띭킎'.split('_'),
        weekdaysShort : '石왽늅石�돔_石ㅰ늉石쇸탫石뺖돗_石싟탨石듀탫石�_石о탛石㏅돐_石듀탫石�늅石닮큲_石듀탣石녀탫石녀늉_石뜩뇽石�'.split('_'),
        weekdaysMin : '石왽늅_石ㅰ늉_石싟탨_石о탛_石듀탫石�늅_石듀탣_石�'.split('_'),
        longDateFormat : {
            LT : 'A h:mm -石ⓣ탛',
            LTS : 'A h:mm:ss -石ⓣ탛',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY, LT',
            LLLL : 'dddd, D MMMM YYYY, LT'
        },
        calendar : {
            sameDay : '[石뉋뇽碩띭뇽碩�] LT',
            nextDay : '[石ⓣ늅石녀탣] LT',
            nextWeek : 'dddd, LT',
            lastDay : '[石뉋뇽碩띭뇽石꿋탣] LT',
            lastWeek : '[石뺖눼石욈킒碩띭킒] dddd, LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s 石뺖눼石욈킒碩띭킒碩�',
            past : '%s 石�탛碩삑눅碩�',
            s : '石끶돕石� 石ⓣ늉石�늉石룅킍碩띭킍碩�',
            m : '石믞눗碩� 石�늉石ⓣ늉石긍탫石긍탫',
            mm : '%d 石�늉石ⓣ늉石긍탫石긍탫',
            h : '石믞눗碩� 石�뇩石욈킉碩띭킉碩귖돔',
            hh : '%d 石�뇩石욈킉碩띭킉碩귖돔',
            d : '石믞눗碩� 石╆늉石듀뉨石�',
            dd : '%d 石╆늉石듀뉨石�',
            M : '石믞눗碩� 石�늅石멘큲',
            MM : '%d 石�늅石멘큲',
            y : '石믞눗碩� 石듀돔石룅큲',
            yy : '%d 石듀돔石룅큲'
        },
        meridiemParse: /石겯늅石ㅰ탫石겯늉|石겯늅石듀늉石꿋탣|石됢킎碩띭킎 石뺖눼石욈킒碩띭킒碩�|石듀탦石뺖탛石ⓣ탫石ⓣ탥石겯큲|石겯늅石ㅰ탫石겯늉/i,
        isPM : function (input) {
            return /^(石됢킎碩띭킎 石뺖눼石욈킒碩띭킒碩�|石듀탦石뺖탛石ⓣ탫石ⓣ탥石겯큲|石겯늅石ㅰ탫石겯늉)$/.test(input);
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 4) {
                return '石겯늅石ㅰ탫石겯늉';
            } else if (hour < 12) {
                return '石겯늅石듀늉石꿋탣';
            } else if (hour < 17) {
                return '石됢킎碩띭킎 石뺖눼石욈킒碩띭킒碩�';
            } else if (hour < 20) {
                return '石듀탦石뺖탛石ⓣ탫石ⓣ탥石겯큲';
            } else {
                return '石겯늅石ㅰ탫石겯늉';
            }
        }
    });
}));
// moment.js locale configuration
// locale : Marathi (mr)
// author : Harshad Kale : https://github.com/kalehv

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '誓�',
        '2': '誓�',
        '3': '誓�',
        '4': '誓�',
        '5': '誓�',
        '6': '誓�',
        '7': '誓�',
        '8': '誓�',
        '9': '誓�',
        '0': '誓�'
    },
    numberMap = {
        '誓�': '1',
        '誓�': '2',
        '誓�': '3',
        '誓�': '4',
        '誓�': '5',
        '誓�': '6',
        '誓�': '7',
        '誓�': '8',
        '誓�': '9',
        '誓�': '0'
    };

    return moment.defineLocale('mr', {
        months : '西쒉ㅎ西ⓣ쪍西듀ㅎ西겯�_西ム쪍西о쪓西겯쪇西듀ㅎ西겯�_西�ㅎ西겯쪓西�_西뤲ㄺ誓띭ㅀ西욈ㅂ_西�쪍_西쒉쪈西�_西쒉쪇西꿋쪎_西묂쨽西멘쪓西�_西멘ㄺ誓띭쩅誓뉋쨧西оㅀ_西묂쨻誓띭쩅誓뗠ㄼ西�_西ⓣ쪑西듀쪓西밝쪍西귖ㄼ西�_西□ㅏ西멘쪍西귖ㄼ西�'.split('_'),
        monthsShort: '西쒉ㅎ西ⓣ쪍._西ム쪍西о쪓西겯쪇._西�ㅎ西겯쪓西�._西뤲ㄺ誓띭ㅀ西�._西�쪍._西쒉쪈西�._西쒉쪇西꿋쪎._西묂쨽._西멘ㄺ誓띭쩅誓뉋쨧._西묂쨻誓띭쩅誓�._西ⓣ쪑西듀쪓西밝쪍西�._西□ㅏ西멘쪍西�.'.split('_'),
        weekdays : '西겯ㅅ西욈ㅅ西약ㅀ_西멘쪑西�ㅅ西약ㅀ_西�쨧西쀠ㅃ西듀ㅎ西�_西о쪇西㏅ㅅ西약ㅀ_西쀠쪇西겯쪈西듀ㅎ西�_西뜩쪇西뺖쪓西겯ㅅ西약ㅀ_西뜩ㄸ西욈ㅅ西약ㅀ'.split('_'),
        weekdaysShort : '西겯ㅅ西�_西멘쪑西�_西�쨧西쀠ㅃ_西о쪇西�_西쀠쪇西겯쪈_西뜩쪇西뺖쪓西�_西뜩ㄸ西�'.split('_'),
        weekdaysMin : '西�_西멘쪑_西�쨧_西о쪇_西쀠쪇_西뜩쪇_西�'.split('_'),
        longDateFormat : {
            LT : 'A h:mm 西듀ㅎ西쒉ㄴ西�',
            LTS : 'A h:mm:ss 西듀ㅎ西쒉ㄴ西�',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY, LT',
            LLLL : 'dddd, D MMMM YYYY, LT'
        },
        calendar : {
            sameDay : '[西녱쩂] LT',
            nextDay : '[西됢ㄶ誓띭ㄿ西�] LT',
            nextWeek : 'dddd, LT',
            lastDay : '[西뺖ㅎ西�] LT',
            lastWeek: '[西�ㅎ西쀠�西�] dddd, LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s 西ⓣ쨧西ㅰㅀ',
            past : '%s 西む쪈西겯쪓西듀�',
            s : '西멘쪍西뺖쨧西�',
            m: '西뤲쨻 西�ㅏ西ⓣㅏ西�',
            mm: '%d 西�ㅏ西ⓣㅏ西잀쪍',
            h : '西뤲쨻 西ㅰㅎ西�',
            hh : '%d 西ㅰㅎ西�',
            d : '西뤲쨻 西╆ㅏ西듀ㅈ',
            dd : '%d 西╆ㅏ西듀ㅈ',
            M : '西뤲쨻 西�ㅉ西욈ㄸ西�',
            MM : '%d 西�ㅉ西욈ㄸ誓�',
            y : '西뤲쨻 西듀ㅀ誓띭ㅇ',
            yy : '%d 西듀ㅀ誓띭ㅇ誓�'
        },
        preparse: function (string) {
            return string.replace(/[誓㏅ⅷ誓⒯ⅹ誓ム�誓��誓�ⅵ]/g, function (match) {
                return numberMap[match];
            });
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            });
        },
        meridiemParse: /西겯ㅎ西ㅰ쪓西겯�|西멘쨻西약ㅃ誓|西╆쪇西むㅎ西겯�|西멘ㅎ西�쨧西뺖ㅎ西녀�/,
        meridiemHour : function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === '西겯ㅎ西ㅰ쪓西겯�') {
                return hour < 4 ? hour : hour + 12;
            } else if (meridiem === '西멘쨻西약ㅃ誓') {
                return hour;
            } else if (meridiem === '西╆쪇西むㅎ西겯�') {
                return hour >= 10 ? hour : hour + 12;
            } else if (meridiem === '西멘ㅎ西�쨧西뺖ㅎ西녀�') {
                return hour + 12;
            }
        },
        meridiem: function (hour, minute, isLower)
        {
            if (hour < 4) {
                return '西겯ㅎ西ㅰ쪓西겯�';
            } else if (hour < 10) {
                return '西멘쨻西약ㅃ誓';
            } else if (hour < 17) {
                return '西╆쪇西むㅎ西겯�';
            } else if (hour < 20) {
                return '西멘ㅎ西�쨧西뺖ㅎ西녀�';
            } else {
                return '西겯ㅎ西ㅰ쪓西겯�';
            }
        },
        week : {
            dow : 0, // Sunday is the first day of the week.
            doy : 6  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Bahasa Malaysia (ms-MY)
// author : Weldan Jamili : https://github.com/weldan

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('ms-my', {
        months : 'Januari_Februari_Mac_April_Mei_Jun_Julai_Ogos_September_Oktober_November_Disember'.split('_'),
        monthsShort : 'Jan_Feb_Mac_Apr_Mei_Jun_Jul_Ogs_Sep_Okt_Nov_Dis'.split('_'),
        weekdays : 'Ahad_Isnin_Selasa_Rabu_Khamis_Jumaat_Sabtu'.split('_'),
        weekdaysShort : 'Ahd_Isn_Sel_Rab_Kha_Jum_Sab'.split('_'),
        weekdaysMin : 'Ah_Is_Sl_Rb_Km_Jm_Sb'.split('_'),
        longDateFormat : {
            LT : 'HH.mm',
            LTS : 'LT.ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY [pukul] LT',
            LLLL : 'dddd, D MMMM YYYY [pukul] LT'
        },
        meridiemParse: /pagi|tengahari|petang|malam/,
        meridiemHour: function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === 'pagi') {
                return hour;
            } else if (meridiem === 'tengahari') {
                return hour >= 11 ? hour : hour + 12;
            } else if (meridiem === 'petang' || meridiem === 'malam') {
                return hour + 12;
            }
        },
        meridiem : function (hours, minutes, isLower) {
            if (hours < 11) {
                return 'pagi';
            } else if (hours < 15) {
                return 'tengahari';
            } else if (hours < 19) {
                return 'petang';
            } else {
                return 'malam';
            }
        },
        calendar : {
            sameDay : '[Hari ini pukul] LT',
            nextDay : '[Esok pukul] LT',
            nextWeek : 'dddd [pukul] LT',
            lastDay : '[Kelmarin pukul] LT',
            lastWeek : 'dddd [lepas pukul] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'dalam %s',
            past : '%s yang lepas',
            s : 'beberapa saat',
            m : 'seminit',
            mm : '%d minit',
            h : 'sejam',
            hh : '%d jam',
            d : 'sehari',
            dd : '%d hari',
            M : 'sebulan',
            MM : '%d bulan',
            y : 'setahun',
            yy : '%d tahun'
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Burmese (my)
// author : Squar team, mysquar.com

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '��',
        '2': '��',
        '3': '��',
        '4': '��',
        '5': '��',
        '6': '��',
        '7': '��',
        '8': '��',
        '9': '��',
        '0': '�'
    }, numberMap = {
        '��': '1',
        '��': '2',
        '��': '3',
        '��': '4',
        '��': '5',
        '��': '6',
        '��': '7',
        '��': '8',
        '��': '9',
        '�': '0'
    };
    return moment.defineLocale('my', {
        months: '�뉌붳뷘붳앩メ쎺�_�뽥긔뽥긔п뷘앩メ쎺�_�쇹먤�_�㎭뺗솽�_�쇹�_�뉌써붳�_�뉌결쒊��꾞�_�왾솽귗�먤�_�끷�뷘먤꾞뷘섂�_�■긔п�뷘먤��섂�_�붳��앩꾞뷘섂�_�믟�뉌꾞뷘섂�'.split('_'),
        monthsShort: '�뉌붳�_�뽥�_�쇹먤�_�뺗솽�_�쇹�_�뉌써붳�_�쒊��꾞�_�왾�_�끷��_�■긔п��_�붳��_�믟�'.split('_'),
        weekdays: '�먤붳꾞뷘밞귗붳써�_�먤붳꾞뷘밞쒊�_�■꾞뷘밞귗�_�쀡�믟밞볚잁결�_��솽п왾뺗먤긔�_�왾긔п�솽�_�끷붳�'.split('_'),
        weekdaysShort: '�붳써�_�쒊�_�꾞뷘밞귗�_�잁결�_��솽�_�왾긔�_�붳�'.split('_'),
        weekdaysMin: '�붳써�_�쒊�_�꾞뷘밞귗�_�잁결�_��솽�_�왾긔�_�붳�'.split('_'),
        longDateFormat: {
            LT: 'HH:mm',
            LTS: 'HH:mm:ss',
            L: 'DD/MM/YYYY',
            LL: 'D MMMM YYYY',
            LLL: 'D MMMM YYYY LT',
            LLLL: 'dddd D MMMM YYYY LT'
        },
        calendar: {
            sameDay: '[�싡붳�.] LT [�쇹얀�]',
            nextDay: '[�쇹붳�뷘뽥솽붳�] LT [�쇹얀�]',
            nextWeek: 'dddd LT [�쇹얀�]',
            lastDay: '[�쇹붳�.�] LT [�쇹얀�]',
            lastWeek: '[�뺗솽�멜곢꿍료왾긔�] dddd LT [�쇹얀�]',
            sameElse: 'L'
        },
        relativeTime: {
            future: '�쒊п쇹듻뷘� %s �쇹얀�',
            past: '�쒊써붳뷘곢꿍료왾긔� %s �',
            s: '�끷�밞�붳�.�■붳듻뷘멜꾞싡�',
            m: '�먤끷뷘쇹�붳끷�',
            mm: '%d �쇹�붳끷�',
            h: '�먤끷뷘붳п쎺�',
            hh: '%d �붳п쎺�',
            d: '�먤끷뷘쎺��',
            dd: '%d �쎺��',
            M: '�먤끷뷘�',
            MM: '%d ��',
            y: '�먤끷뷘붳얀끷�',
            yy: '%d �붳얀끷�'
        },
        preparse: function (string) {
            return string.replace(/[�곢걗�꺻걚�끷걝�뉌걟�됣�]/g, function (match) {
                return numberMap[match];
            });
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            });
        },
        week: {
            dow: 1, // Monday is the first day of the week.
            doy: 4 // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : norwegian bokm책l (nb)
// authors : Espen Hovlandsdal : https://github.com/rexxars
//           Sigurd Gartmann : https://github.com/sigurdga

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('nb', {
        months : 'januar_februar_mars_april_mai_juni_juli_august_september_oktober_november_desember'.split('_'),
        monthsShort : 'jan_feb_mar_apr_mai_jun_jul_aug_sep_okt_nov_des'.split('_'),
        weekdays : 's첩ndag_mandag_tirsdag_onsdag_torsdag_fredag_l첩rdag'.split('_'),
        weekdaysShort : 's첩n_man_tirs_ons_tors_fre_l첩r'.split('_'),
        weekdaysMin : 's첩_ma_ti_on_to_fr_l첩'.split('_'),
        longDateFormat : {
            LT : 'H.mm',
            LTS : 'LT.ss',
            L : 'DD.MM.YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY [kl.] LT',
            LLLL : 'dddd D. MMMM YYYY [kl.] LT'
        },
        calendar : {
            sameDay: '[i dag kl.] LT',
            nextDay: '[i morgen kl.] LT',
            nextWeek: 'dddd [kl.] LT',
            lastDay: '[i g책r kl.] LT',
            lastWeek: '[forrige] dddd [kl.] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'om %s',
            past : 'for %s siden',
            s : 'noen sekunder',
            m : 'ett minutt',
            mm : '%d minutter',
            h : 'en time',
            hh : '%d timer',
            d : 'en dag',
            dd : '%d dager',
            M : 'en m책ned',
            MM : '%d m책neder',
            y : 'ett 책r',
            yy : '%d 책r'
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : nepali/nepalese
// author : suvash : https://github.com/suvash

(function (factory) {
    factory(moment);
}(function (moment) {
    var symbolMap = {
        '1': '誓�',
        '2': '誓�',
        '3': '誓�',
        '4': '誓�',
        '5': '誓�',
        '6': '誓�',
        '7': '誓�',
        '8': '誓�',
        '9': '誓�',
        '0': '誓�'
    },
    numberMap = {
        '誓�': '1',
        '誓�': '2',
        '誓�': '3',
        '誓�': '4',
        '誓�': '5',
        '誓�': '6',
        '誓�': '7',
        '誓�': '8',
        '誓�': '9',
        '誓�': '0'
    };

    return moment.defineLocale('ne', {
        months : '西쒉ㄸ西듀ㅀ誓_西ム쪍西о쪓西겯쪇西듀ㅀ誓_西�ㅎ西겯쪓西�_西끶ㄺ誓띭ㅀ西욈ㅂ_西�쨮_西쒉쪇西�_西쒉쪇西꿋ㅎ西�_西끶쨽西룅쪓西�_西멘쪍西む쪓西잀쪍西�쪓西оㅀ_西끶쨻誓띭쩅誓뗠ㄼ西�_西ⓣ쪑西�쪍西�쪓西оㅀ_西□ㅏ西멘쪍西�쪓西оㅀ'.split('_'),
        monthsShort : '西쒉ㄸ._西ム쪍西о쪓西겯쪇._西�ㅎ西겯쪓西�_西끶ㄺ誓띭ㅀ西�._西�쨮_西쒉쪇西�_西쒉쪇西꿋ㅎ西�._西끶쨽._西멘쪍西む쪓西�._西끶쨻誓띭쩅誓�._西ⓣ쪑西�쪍._西□ㅏ西멘쪍.'.split('_'),
        weekdays : '西녱쨭西ㅰㄼ西약ㅀ_西멘쪑西�ㄼ西약ㅀ_西�쨿誓띭쨽西꿋ㄼ西약ㅀ_西о쪇西㏅ㄼ西약ㅀ_西оㅏ西밝ㅏ西оㅎ西�_西뜩쪇西뺖쪓西겯ㄼ西약ㅀ_西뜩ㄸ西욈ㄼ西약ㅀ'.split('_'),
        weekdaysShort : '西녱쨭西�._西멘쪑西�._西�쨿誓띭쨽西�._西о쪇西�._西оㅏ西밝ㅏ._西뜩쪇西뺖쪓西�._西뜩ㄸ西�.'.split('_'),
        weekdaysMin : '西녱쨭._西멘쪑._西�쨿誓�_西о쪇._西оㅏ._西뜩쪇._西�.'.split('_'),
        longDateFormat : {
            LT : 'A西뺖쪑 h:mm 西о쩂誓�',
            LTS : 'A西뺖쪑 h:mm:ss 西о쩂誓�',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY, LT',
            LLLL : 'dddd, D MMMM YYYY, LT'
        },
        preparse: function (string) {
            return string.replace(/[誓㏅ⅷ誓⒯ⅹ誓ム�誓��誓�ⅵ]/g, function (match) {
                return numberMap[match];
            });
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            });
        },
        meridiemParse: /西겯ㅎ西ㅰ�|西оㅏ西밝ㅎ西�|西╆ㅏ西됢쨦西멘쪑|西о쪍西꿋쪇西뺖ㅎ|西멘ㅎ西곟쩃|西겯ㅎ西ㅰ�/,
        meridiemHour : function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === '西겯ㅎ西ㅰ�') {
                return hour < 3 ? hour : hour + 12;
            } else if (meridiem === '西оㅏ西밝ㅎ西�') {
                return hour;
            } else if (meridiem === '西╆ㅏ西됢쨦西멘쪑') {
                return hour >= 10 ? hour : hour + 12;
            } else if (meridiem === '西о쪍西꿋쪇西뺖ㅎ' || meridiem === '西멘ㅎ西곟쩃') {
                return hour + 12;
            }
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 3) {
                return '西겯ㅎ西ㅰ�';
            } else if (hour < 10) {
                return '西оㅏ西밝ㅎ西�';
            } else if (hour < 15) {
                return '西╆ㅏ西됢쨦西멘쪑';
            } else if (hour < 18) {
                return '西о쪍西꿋쪇西뺖ㅎ';
            } else if (hour < 20) {
                return '西멘ㅎ西곟쩃';
            } else {
                return '西겯ㅎ西ㅰ�';
            }
        },
        calendar : {
            sameDay : '[西녱쩂] LT',
            nextDay : '[西�쪑西꿋�] LT',
            nextWeek : '[西녱쨯西곟ㄶ誓�] dddd[,] LT',
            lastDay : '[西밝ㅏ西쒉쪑] LT',
            lastWeek : '[西쀠쨵西뺖쪑] dddd[,] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s西�ㅎ',
            past : '%s 西끶쨽西약ㄱ誓',
            s : '西뺖쪍西밝� 西멘ㄾ西�',
            m : '西뤲쨻 西�ㅏ西ⓣ쪍西�',
            mm : '%d 西�ㅏ西ⓣ쪍西�',
            h : '西뤲쨻 西섁ㄳ誓띭쩅西�',
            hh : '%d 西섁ㄳ誓띭쩅西�',
            d : '西뤲쨻 西╆ㅏ西�',
            dd : '%d 西╆ㅏ西�',
            M : '西뤲쨻 西�ㅉ西욈ㄸ西�',
            MM : '%d 西�ㅉ西욈ㄸ西�',
            y : '西뤲쨻 西оㅀ誓띭ㅇ',
            yy : '%d 西оㅀ誓띭ㅇ'
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : dutch (nl)
// author : Joris R철ling : https://github.com/jjupiter

(function (factory) {
    factory(moment);
}(function (moment) {
    var monthsShortWithDots = 'jan._feb._mrt._apr._mei_jun._jul._aug._sep._okt._nov._dec.'.split('_'),
        monthsShortWithoutDots = 'jan_feb_mrt_apr_mei_jun_jul_aug_sep_okt_nov_dec'.split('_');

    return moment.defineLocale('nl', {
        months : 'januari_februari_maart_april_mei_juni_juli_augustus_september_oktober_november_december'.split('_'),
        monthsShort : function (m, format) {
            if (/-MMM-/.test(format)) {
                return monthsShortWithoutDots[m.month()];
            } else {
                return monthsShortWithDots[m.month()];
            }
        },
        weekdays : 'zondag_maandag_dinsdag_woensdag_donderdag_vrijdag_zaterdag'.split('_'),
        weekdaysShort : 'zo._ma._di._wo._do._vr._za.'.split('_'),
        weekdaysMin : 'Zo_Ma_Di_Wo_Do_Vr_Za'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD-MM-YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[vandaag om] LT',
            nextDay: '[morgen om] LT',
            nextWeek: 'dddd [om] LT',
            lastDay: '[gisteren om] LT',
            lastWeek: '[afgelopen] dddd [om] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'over %s',
            past : '%s geleden',
            s : 'een paar seconden',
            m : '챕챕n minuut',
            mm : '%d minuten',
            h : '챕챕n uur',
            hh : '%d uur',
            d : '챕챕n dag',
            dd : '%d dagen',
            M : '챕챕n maand',
            MM : '%d maanden',
            y : '챕챕n jaar',
            yy : '%d jaar'
        },
        ordinalParse: /\d{1,2}(ste|de)/,
        ordinal : function (number) {
            return number + ((number === 1 || number === 8 || number >= 20) ? 'ste' : 'de');
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : norwegian nynorsk (nn)
// author : https://github.com/mechuwind

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('nn', {
        months : 'januar_februar_mars_april_mai_juni_juli_august_september_oktober_november_desember'.split('_'),
        monthsShort : 'jan_feb_mar_apr_mai_jun_jul_aug_sep_okt_nov_des'.split('_'),
        weekdays : 'sundag_m책ndag_tysdag_onsdag_torsdag_fredag_laurdag'.split('_'),
        weekdaysShort : 'sun_m책n_tys_ons_tor_fre_lau'.split('_'),
        weekdaysMin : 'su_m책_ty_on_to_fr_l첩'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[I dag klokka] LT',
            nextDay: '[I morgon klokka] LT',
            nextWeek: 'dddd [klokka] LT',
            lastDay: '[I g책r klokka] LT',
            lastWeek: '[F첩reg책ande] dddd [klokka] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'om %s',
            past : 'for %s sidan',
            s : 'nokre sekund',
            m : 'eit minutt',
            mm : '%d minutt',
            h : 'ein time',
            hh : '%d timar',
            d : 'ein dag',
            dd : '%d dagar',
            M : 'ein m책nad',
            MM : '%d m책nader',
            y : 'eit 책r',
            yy : '%d 책r'
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : polish (pl)
// author : Rafal Hirsz : https://github.com/evoL

(function (factory) {
    factory(moment);
}(function (moment) {
    var monthsNominative = 'stycze흦_luty_marzec_kwiecie흦_maj_czerwiec_lipiec_sierpie흦_wrzesie흦_pa탄dziernik_listopad_grudzie흦'.split('_'),
        monthsSubjective = 'stycznia_lutego_marca_kwietnia_maja_czerwca_lipca_sierpnia_wrze힄nia_pa탄dziernika_listopada_grudnia'.split('_');

    function plural(n) {
        return (n % 10 < 5) && (n % 10 > 1) && ((~~(n / 10) % 10) !== 1);
    }

    function translate(number, withoutSuffix, key) {
        var result = number + ' ';
        switch (key) {
        case 'm':
            return withoutSuffix ? 'minuta' : 'minut휌';
        case 'mm':
            return result + (plural(number) ? 'minuty' : 'minut');
        case 'h':
            return withoutSuffix  ? 'godzina'  : 'godzin휌';
        case 'hh':
            return result + (plural(number) ? 'godziny' : 'godzin');
        case 'MM':
            return result + (plural(number) ? 'miesi훳ce' : 'miesi휌cy');
        case 'yy':
            return result + (plural(number) ? 'lata' : 'lat');
        }
    }

    return moment.defineLocale('pl', {
        months : function (momentToFormat, format) {
            if (/D MMMM/.test(format)) {
                return monthsSubjective[momentToFormat.month()];
            } else {
                return monthsNominative[momentToFormat.month()];
            }
        },
        monthsShort : 'sty_lut_mar_kwi_maj_cze_lip_sie_wrz_pa탄_lis_gru'.split('_'),
        weekdays : 'niedziela_poniedzia흢ek_wtorek_힄roda_czwartek_pi훳tek_sobota'.split('_'),
        weekdaysShort : 'nie_pon_wt_힄r_czw_pt_sb'.split('_'),
        weekdaysMin : 'N_Pn_Wt_힃r_Cz_Pt_So'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[Dzi힄 o] LT',
            nextDay: '[Jutro o] LT',
            nextWeek: '[W] dddd [o] LT',
            lastDay: '[Wczoraj o] LT',
            lastWeek: function () {
                switch (this.day()) {
                case 0:
                    return '[W zesz흢훳 niedziel휌 o] LT';
                case 3:
                    return '[W zesz흢훳 힄rod휌 o] LT';
                case 6:
                    return '[W zesz흢훳 sobot휌 o] LT';
                default:
                    return '[W zesz흢y] dddd [o] LT';
                }
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : 'za %s',
            past : '%s temu',
            s : 'kilka sekund',
            m : translate,
            mm : translate,
            h : translate,
            hh : translate,
            d : '1 dzie흦',
            dd : '%d dni',
            M : 'miesi훳c',
            MM : translate,
            y : 'rok',
            yy : translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : brazilian portuguese (pt-br)
// author : Caio Ribeiro Pereira : https://github.com/caio-ribeiro-pereira

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('pt-br', {
        months : 'janeiro_fevereiro_mar챌o_abril_maio_junho_julho_agosto_setembro_outubro_novembro_dezembro'.split('_'),
        monthsShort : 'jan_fev_mar_abr_mai_jun_jul_ago_set_out_nov_dez'.split('_'),
        weekdays : 'domingo_segunda-feira_ter챌a-feira_quarta-feira_quinta-feira_sexta-feira_s찼bado'.split('_'),
        weekdaysShort : 'dom_seg_ter_qua_qui_sex_s찼b'.split('_'),
        weekdaysMin : 'dom_2짧_3짧_4짧_5짧_6짧_s찼b'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D [de] MMMM [de] YYYY',
            LLL : 'D [de] MMMM [de] YYYY [횪s] LT',
            LLLL : 'dddd, D [de] MMMM [de] YYYY [횪s] LT'
        },
        calendar : {
            sameDay: '[Hoje 횪s] LT',
            nextDay: '[Amanh찾 횪s] LT',
            nextWeek: 'dddd [횪s] LT',
            lastDay: '[Ontem 횪s] LT',
            lastWeek: function () {
                return (this.day() === 0 || this.day() === 6) ?
                    '[횣ltimo] dddd [횪s] LT' : // Saturday + Sunday
                    '[횣ltima] dddd [횪s] LT'; // Monday - Friday
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : 'em %s',
            past : '%s atr찼s',
            s : 'segundos',
            m : 'um minuto',
            mm : '%d minutos',
            h : 'uma hora',
            hh : '%d horas',
            d : 'um dia',
            dd : '%d dias',
            M : 'um m챗s',
            MM : '%d meses',
            y : 'um ano',
            yy : '%d anos'
        },
        ordinalParse: /\d{1,2}쨘/,
        ordinal : '%d쨘'
    });
}));
// moment.js locale configuration
// locale : portuguese (pt)
// author : Jefferson : https://github.com/jalex79

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('pt', {
        months : 'janeiro_fevereiro_mar챌o_abril_maio_junho_julho_agosto_setembro_outubro_novembro_dezembro'.split('_'),
        monthsShort : 'jan_fev_mar_abr_mai_jun_jul_ago_set_out_nov_dez'.split('_'),
        weekdays : 'domingo_segunda-feira_ter챌a-feira_quarta-feira_quinta-feira_sexta-feira_s찼bado'.split('_'),
        weekdaysShort : 'dom_seg_ter_qua_qui_sex_s찼b'.split('_'),
        weekdaysMin : 'dom_2짧_3짧_4짧_5짧_6짧_s찼b'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D [de] MMMM [de] YYYY',
            LLL : 'D [de] MMMM [de] YYYY LT',
            LLLL : 'dddd, D [de] MMMM [de] YYYY LT'
        },
        calendar : {
            sameDay: '[Hoje 횪s] LT',
            nextDay: '[Amanh찾 횪s] LT',
            nextWeek: 'dddd [횪s] LT',
            lastDay: '[Ontem 횪s] LT',
            lastWeek: function () {
                return (this.day() === 0 || this.day() === 6) ?
                    '[횣ltimo] dddd [횪s] LT' : // Saturday + Sunday
                    '[횣ltima] dddd [횪s] LT'; // Monday - Friday
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : 'em %s',
            past : 'h찼 %s',
            s : 'segundos',
            m : 'um minuto',
            mm : '%d minutos',
            h : 'uma hora',
            hh : '%d horas',
            d : 'um dia',
            dd : '%d dias',
            M : 'um m챗s',
            MM : '%d meses',
            y : 'um ano',
            yy : '%d anos'
        },
        ordinalParse: /\d{1,2}쨘/,
        ordinal : '%d쨘',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : romanian (ro)
// author : Vlad Gurdiga : https://github.com/gurdiga
// author : Valentin Agachi : https://github.com/avaly

(function (factory) {
    factory(moment);
}(function (moment) {
    function relativeTimeWithPlural(number, withoutSuffix, key) {
        var format = {
                'mm': 'minute',
                'hh': 'ore',
                'dd': 'zile',
                'MM': 'luni',
                'yy': 'ani'
            },
            separator = ' ';
        if (number % 100 >= 20 || (number >= 100 && number % 100 === 0)) {
            separator = ' de ';
        }

        return number + separator + format[key];
    }

    return moment.defineLocale('ro', {
        months : 'ianuarie_februarie_martie_aprilie_mai_iunie_iulie_august_septembrie_octombrie_noiembrie_decembrie'.split('_'),
        monthsShort : 'ian._febr._mart._apr._mai_iun._iul._aug._sept._oct._nov._dec.'.split('_'),
        weekdays : 'duminic훱_luni_mar�i_miercuri_joi_vineri_s창mb훱t훱'.split('_'),
        weekdaysShort : 'Dum_Lun_Mar_Mie_Joi_Vin_S창m'.split('_'),
        weekdaysMin : 'Du_Lu_Ma_Mi_Jo_Vi_S창'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY H:mm',
            LLLL : 'dddd, D MMMM YYYY H:mm'
        },
        calendar : {
            sameDay: '[azi la] LT',
            nextDay: '[m창ine la] LT',
            nextWeek: 'dddd [la] LT',
            lastDay: '[ieri la] LT',
            lastWeek: '[fosta] dddd [la] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'peste %s',
            past : '%s 챤n urm훱',
            s : 'c창teva secunde',
            m : 'un minut',
            mm : relativeTimeWithPlural,
            h : 'o or훱',
            hh : relativeTimeWithPlural,
            d : 'o zi',
            dd : relativeTimeWithPlural,
            M : 'o lun훱',
            MM : relativeTimeWithPlural,
            y : 'un an',
            yy : relativeTimeWithPlural
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : russian (ru)
// author : Viktorminator : https://github.com/Viktorminator
// Author : Menelion Elens첬le : https://github.com/Oire

(function (factory) {
    factory(moment);
}(function (moment) {
    function plural(word, num) {
        var forms = word.split('_');
        return num % 10 === 1 && num % 100 !== 11 ? forms[0] : (num % 10 >= 2 && num % 10 <= 4 && (num % 100 < 10 || num % 100 >= 20) ? forms[1] : forms[2]);
    }

    function relativeTimeWithPlural(number, withoutSuffix, key) {
        var format = {
            'mm': withoutSuffix ? '劇龜戟��逵_劇龜戟���_劇龜戟��' : '劇龜戟���_劇龜戟���_劇龜戟��',
            'hh': '�逵�_�逵�逵_�逵�棘勻',
            'dd': '畇筠戟�_畇戟�_畇戟筠橘',
            'MM': '劇筠���_劇筠���逵_劇筠���筠勻',
            'yy': '均棘畇_均棘畇逵_剋筠�'
        };
        if (key === 'm') {
            return withoutSuffix ? '劇龜戟��逵' : '劇龜戟���';
        }
        else {
            return number + ' ' + plural(format[key], +number);
        }
    }

    function monthsCaseReplace(m, format) {
        var months = {
            'nominative': '�戟勻逵��_�筠勻�逵剋�_劇逵��_逵極�筠剋�_劇逵橘_龜�戟�_龜�剋�_逵勻均���_�筠戟��閨��_棘克��閨��_戟棘�閨��_畇筠克逵閨��'.split('_'),
            'accusative': '�戟勻逵��_�筠勻�逵剋�_劇逵��逵_逵極�筠剋�_劇逵�_龜�戟�_龜�剋�_逵勻均���逵_�筠戟��閨��_棘克��閨��_戟棘�閨��_畇筠克逵閨��'.split('_')
        },

        nounCase = (/D[oD]?(\[[^\[\]]*\]|\s+)+MMMM?/).test(format) ?
            'accusative' :
            'nominative';

        return months[nounCase][m.month()];
    }

    function monthsShortCaseReplace(m, format) {
        var monthsShort = {
            'nominative': '�戟勻_�筠勻_劇逵��_逵極�_劇逵橘_龜�戟�_龜�剋�_逵勻均_�筠戟_棘克�_戟棘�_畇筠克'.split('_'),
            'accusative': '�戟勻_�筠勻_劇逵�_逵極�_劇逵�_龜�戟�_龜�剋�_逵勻均_�筠戟_棘克�_戟棘�_畇筠克'.split('_')
        },

        nounCase = (/D[oD]?(\[[^\[\]]*\]|\s+)+MMMM?/).test(format) ?
            'accusative' :
            'nominative';

        return monthsShort[nounCase][m.month()];
    }

    function weekdaysCaseReplace(m, format) {
        var weekdays = {
            'nominative': '勻棘�克�筠�筠戟�筠_極棘戟筠畇筠剋�戟龜克_勻�棘�戟龜克_��筠畇逵_�筠�勻筠�均_極��戟龜�逵_��閨閨棘�逵'.split('_'),
            'accusative': '勻棘�克�筠�筠戟�筠_極棘戟筠畇筠剋�戟龜克_勻�棘�戟龜克_��筠畇�_�筠�勻筠�均_極��戟龜��_��閨閨棘��'.split('_')
        },

        nounCase = (/\[ ?[�勻] ?(?:極�棘�剋��|�剋筠畇�����|���)? ?\] ?dddd/).test(format) ?
            'accusative' :
            'nominative';

        return weekdays[nounCase][m.day()];
    }

    return moment.defineLocale('ru', {
        months : monthsCaseReplace,
        monthsShort : monthsShortCaseReplace,
        weekdays : weekdaysCaseReplace,
        weekdaysShort : '勻�_極戟_勻�_��_��_極�_�閨'.split('_'),
        weekdaysMin : '勻�_極戟_勻�_��_��_極�_�閨'.split('_'),
        monthsParse : [/^�戟勻/i, /^�筠勻/i, /^劇逵�/i, /^逵極�/i, /^劇逵[橘|�]/i, /^龜�戟/i, /^龜�剋/i, /^逵勻均/i, /^�筠戟/i, /^棘克�/i, /^戟棘�/i, /^畇筠克/i],
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY 均.',
            LLL : 'D MMMM YYYY 均., LT',
            LLLL : 'dddd, D MMMM YYYY 均., LT'
        },
        calendar : {
            sameDay: '[鬼筠均棘畇戟� 勻] LT',
            nextDay: '[�逵勻��逵 勻] LT',
            lastDay: '[��筠�逵 勻] LT',
            nextWeek: function () {
                return this.day() === 2 ? '[�棘] dddd [勻] LT' : '[�] dddd [勻] LT';
            },
            lastWeek: function (now) {
                if (now.week() !== this.week()) {
                    switch (this.day()) {
                    case 0:
                        return '[� 極�棘�剋棘筠] dddd [勻] LT';
                    case 1:
                    case 2:
                    case 4:
                        return '[� 極�棘�剋�橘] dddd [勻] LT';
                    case 3:
                    case 5:
                    case 6:
                        return '[� 極�棘�剋��] dddd [勻] LT';
                    }
                } else {
                    if (this.day() === 2) {
                        return '[�棘] dddd [勻] LT';
                    } else {
                        return '[�] dddd [勻] LT';
                    }
                }
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : '�筠�筠鈞 %s',
            past : '%s 戟逵鈞逵畇',
            s : '戟筠�克棘剋�克棘 �筠克�戟畇',
            m : relativeTimeWithPlural,
            mm : relativeTimeWithPlural,
            h : '�逵�',
            hh : relativeTimeWithPlural,
            d : '畇筠戟�',
            dd : relativeTimeWithPlural,
            M : '劇筠���',
            MM : relativeTimeWithPlural,
            y : '均棘畇',
            yy : relativeTimeWithPlural
        },

        meridiemParse: /戟棘�龜|���逵|畇戟�|勻筠�筠�逵/i,
        isPM : function (input) {
            return /^(畇戟�|勻筠�筠�逵)$/.test(input);
        },

        meridiem : function (hour, minute, isLower) {
            if (hour < 4) {
                return '戟棘�龜';
            } else if (hour < 12) {
                return '���逵';
            } else if (hour < 17) {
                return '畇戟�';
            } else {
                return '勻筠�筠�逵';
            }
        },

        ordinalParse: /\d{1,2}-(橘|均棘|�)/,
        ordinal: function (number, period) {
            switch (period) {
            case 'M':
            case 'd':
            case 'DDD':
                return number + '-橘';
            case 'D':
                return number + '-均棘';
            case 'w':
            case 'W':
                return number + '-�';
            default:
                return number;
            }
        },

        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : slovak (sk)
// author : Martin Minka : https://github.com/k2s
// based on work of petrbela : https://github.com/petrbela

(function (factory) {
    factory(moment);
}(function (moment) {
    var months = 'janu찼r_febru찼r_marec_apr챠l_m찼j_j첬n_j첬l_august_september_okt처ber_november_december'.split('_'),
        monthsShort = 'jan_feb_mar_apr_m찼j_j첬n_j첬l_aug_sep_okt_nov_dec'.split('_');

    function plural(n) {
        return (n > 1) && (n < 5);
    }

    function translate(number, withoutSuffix, key, isFuture) {
        var result = number + ' ';
        switch (key) {
        case 's':  // a few seconds / in a few seconds / a few seconds ago
            return (withoutSuffix || isFuture) ? 'p찼r sek첬nd' : 'p찼r sekundami';
        case 'm':  // a minute / in a minute / a minute ago
            return withoutSuffix ? 'min첬ta' : (isFuture ? 'min첬tu' : 'min첬tou');
        case 'mm': // 9 minutes / in 9 minutes / 9 minutes ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'min첬ty' : 'min첬t');
            } else {
                return result + 'min첬tami';
            }
            break;
        case 'h':  // an hour / in an hour / an hour ago
            return withoutSuffix ? 'hodina' : (isFuture ? 'hodinu' : 'hodinou');
        case 'hh': // 9 hours / in 9 hours / 9 hours ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'hodiny' : 'hod챠n');
            } else {
                return result + 'hodinami';
            }
            break;
        case 'd':  // a day / in a day / a day ago
            return (withoutSuffix || isFuture) ? 'de흫' : 'd흫om';
        case 'dd': // 9 days / in 9 days / 9 days ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'dni' : 'dn챠');
            } else {
                return result + 'd흫ami';
            }
            break;
        case 'M':  // a month / in a month / a month ago
            return (withoutSuffix || isFuture) ? 'mesiac' : 'mesiacom';
        case 'MM': // 9 months / in 9 months / 9 months ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'mesiace' : 'mesiacov');
            } else {
                return result + 'mesiacmi';
            }
            break;
        case 'y':  // a year / in a year / a year ago
            return (withoutSuffix || isFuture) ? 'rok' : 'rokom';
        case 'yy': // 9 years / in 9 years / 9 years ago
            if (withoutSuffix || isFuture) {
                return result + (plural(number) ? 'roky' : 'rokov');
            } else {
                return result + 'rokmi';
            }
            break;
        }
    }

    return moment.defineLocale('sk', {
        months : months,
        monthsShort : monthsShort,
        monthsParse : (function (months, monthsShort) {
            var i, _monthsParse = [];
            for (i = 0; i < 12; i++) {
                // use custom parser to solve problem with July (훾ervenec)
                _monthsParse[i] = new RegExp('^' + months[i] + '$|^' + monthsShort[i] + '$', 'i');
            }
            return _monthsParse;
        }(months, monthsShort)),
        weekdays : 'nede컁a_pondelok_utorok_streda_큄tvrtok_piatok_sobota'.split('_'),
        weekdaysShort : 'ne_po_ut_st_큄t_pi_so'.split('_'),
        weekdaysMin : 'ne_po_ut_st_큄t_pi_so'.split('_'),
        longDateFormat : {
            LT: 'H:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY LT',
            LLLL : 'dddd D. MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[dnes o] LT',
            nextDay: '[zajtra o] LT',
            nextWeek: function () {
                switch (this.day()) {
                case 0:
                    return '[v nede컁u o] LT';
                case 1:
                case 2:
                    return '[v] dddd [o] LT';
                case 3:
                    return '[v stredu o] LT';
                case 4:
                    return '[vo 큄tvrtok o] LT';
                case 5:
                    return '[v piatok o] LT';
                case 6:
                    return '[v sobotu o] LT';
                }
            },
            lastDay: '[v훾era o] LT',
            lastWeek: function () {
                switch (this.day()) {
                case 0:
                    return '[minul첬 nede컁u o] LT';
                case 1:
                case 2:
                    return '[minul첵] dddd [o] LT';
                case 3:
                    return '[minul첬 stredu o] LT';
                case 4:
                case 5:
                    return '[minul첵] dddd [o] LT';
                case 6:
                    return '[minul첬 sobotu o] LT';
                }
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : 'za %s',
            past : 'pred %s',
            s : translate,
            m : translate,
            mm : translate,
            h : translate,
            hh : translate,
            d : translate,
            dd : translate,
            M : translate,
            MM : translate,
            y : translate,
            yy : translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : slovenian (sl)
// author : Robert Sedov큄ek : https://github.com/sedovsek

(function (factory) {
    factory(moment);
}(function (moment) {
    function translate(number, withoutSuffix, key) {
        var result = number + ' ';
        switch (key) {
        case 'm':
            return withoutSuffix ? 'ena minuta' : 'eno minuto';
        case 'mm':
            if (number === 1) {
                result += 'minuta';
            } else if (number === 2) {
                result += 'minuti';
            } else if (number === 3 || number === 4) {
                result += 'minute';
            } else {
                result += 'minut';
            }
            return result;
        case 'h':
            return withoutSuffix ? 'ena ura' : 'eno uro';
        case 'hh':
            if (number === 1) {
                result += 'ura';
            } else if (number === 2) {
                result += 'uri';
            } else if (number === 3 || number === 4) {
                result += 'ure';
            } else {
                result += 'ur';
            }
            return result;
        case 'dd':
            if (number === 1) {
                result += 'dan';
            } else {
                result += 'dni';
            }
            return result;
        case 'MM':
            if (number === 1) {
                result += 'mesec';
            } else if (number === 2) {
                result += 'meseca';
            } else if (number === 3 || number === 4) {
                result += 'mesece';
            } else {
                result += 'mesecev';
            }
            return result;
        case 'yy':
            if (number === 1) {
                result += 'leto';
            } else if (number === 2) {
                result += 'leti';
            } else if (number === 3 || number === 4) {
                result += 'leta';
            } else {
                result += 'let';
            }
            return result;
        }
    }

    return moment.defineLocale('sl', {
        months : 'januar_februar_marec_april_maj_junij_julij_avgust_september_oktober_november_december'.split('_'),
        monthsShort : 'jan._feb._mar._apr._maj._jun._jul._avg._sep._okt._nov._dec.'.split('_'),
        weekdays : 'nedelja_ponedeljek_torek_sreda_훾etrtek_petek_sobota'.split('_'),
        weekdaysShort : 'ned._pon._tor._sre._훾et._pet._sob.'.split('_'),
        weekdaysMin : 'ne_po_to_sr_훾e_pe_so'.split('_'),
        longDateFormat : {
            LT : 'H:mm',
            LTS : 'LT:ss',
            L : 'DD. MM. YYYY',
            LL : 'D. MMMM YYYY',
            LLL : 'D. MMMM YYYY LT',
            LLLL : 'dddd, D. MMMM YYYY LT'
        },
        calendar : {
            sameDay  : '[danes ob] LT',
            nextDay  : '[jutri ob] LT',

            nextWeek : function () {
                switch (this.day()) {
                case 0:
                    return '[v] [nedeljo] [ob] LT';
                case 3:
                    return '[v] [sredo] [ob] LT';
                case 6:
                    return '[v] [soboto] [ob] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[v] dddd [ob] LT';
                }
            },
            lastDay  : '[v훾eraj ob] LT',
            lastWeek : function () {
                switch (this.day()) {
                case 0:
                case 3:
                case 6:
                    return '[prej큄nja] dddd [ob] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[prej큄nji] dddd [ob] LT';
                }
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : '훾ez %s',
            past   : '%s nazaj',
            s      : 'nekaj sekund',
            m      : translate,
            mm     : translate,
            h      : translate,
            hh     : translate,
            d      : 'en dan',
            dd     : translate,
            M      : 'en mesec',
            MM     : translate,
            y      : 'eno leto',
            yy     : translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Albanian (sq)
// author : Flak챘rim Ismani : https://github.com/flakerimi
// author: Menelion Elens첬le: https://github.com/Oire (tests)
// author : Oerd Cukalla : https://github.com/oerd (fixes)

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('sq', {
        months : 'Janar_Shkurt_Mars_Prill_Maj_Qershor_Korrik_Gusht_Shtator_Tetor_N챘ntor_Dhjetor'.split('_'),
        monthsShort : 'Jan_Shk_Mar_Pri_Maj_Qer_Kor_Gus_Sht_Tet_N챘n_Dhj'.split('_'),
        weekdays : 'E Diel_E H챘n챘_E Mart챘_E M챘rkur챘_E Enjte_E Premte_E Shtun챘'.split('_'),
        weekdaysShort : 'Die_H챘n_Mar_M챘r_Enj_Pre_Sht'.split('_'),
        weekdaysMin : 'D_H_Ma_M챘_E_P_Sh'.split('_'),
        meridiemParse: /PD|MD/,
        isPM: function (input) {
            return input.charAt(0) === 'M';
        },
        meridiem : function (hours, minutes, isLower) {
            return hours < 12 ? 'PD' : 'MD';
        },
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[Sot n챘] LT',
            nextDay : '[Nes챘r n챘] LT',
            nextWeek : 'dddd [n챘] LT',
            lastDay : '[Dje n챘] LT',
            lastWeek : 'dddd [e kaluar n챘] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : 'n챘 %s',
            past : '%s m챘 par챘',
            s : 'disa sekonda',
            m : 'nj챘 minut챘',
            mm : '%d minuta',
            h : 'nj챘 or챘',
            hh : '%d or챘',
            d : 'nj챘 dit챘',
            dd : '%d dit챘',
            M : 'nj챘 muaj',
            MM : '%d muaj',
            y : 'nj챘 vit',
            yy : '%d vite'
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Serbian-cyrillic (sr-cyrl)
// author : Milan Jana훾kovi훶<milanjanackovic@gmail.com> : https://github.com/milan-j

(function (factory) {
    factory(moment);
}(function (moment) {
    var translator = {
        words: { //Different grammatical cases
            m: ['�筠畇逵戟 劇龜戟��', '�筠畇戟筠 劇龜戟��筠'],
            mm: ['劇龜戟��', '劇龜戟��筠', '劇龜戟��逵'],
            h: ['�筠畇逵戟 �逵�', '�筠畇戟棘均 �逵�逵'],
            hh: ['�逵�', '�逵�逵', '�逵�龜'],
            dd: ['畇逵戟', '畇逵戟逵', '畇逵戟逵'],
            MM: ['劇筠�筠�', '劇筠�筠�逵', '劇筠�筠�龜'],
            yy: ['均棘畇龜戟逵', '均棘畇龜戟筠', '均棘畇龜戟逵']
        },
        correctGrammaticalCase: function (number, wordKey) {
            return number === 1 ? wordKey[0] : (number >= 2 && number <= 4 ? wordKey[1] : wordKey[2]);
        },
        translate: function (number, withoutSuffix, key) {
            var wordKey = translator.words[key];
            if (key.length === 1) {
                return withoutSuffix ? wordKey[0] : wordKey[1];
            } else {
                return number + ' ' + translator.correctGrammaticalCase(number, wordKey);
            }
        }
    };

    return moment.defineLocale('sr-cyrl', {
        months: ['�逵戟�逵�', '�筠閨��逵�', '劇逵��', '逵極�龜剋', '劇逵�', '��戟', '��剋', '逵勻均���', '�筠極�筠劇閨逵�', '棘克�棘閨逵�', '戟棘勻筠劇閨逵�', '畇筠�筠劇閨逵�'],
        monthsShort: ['�逵戟.', '�筠閨.', '劇逵�.', '逵極�.', '劇逵�', '��戟', '��剋', '逵勻均.', '�筠極.', '棘克�.', '戟棘勻.', '畇筠�.'],
        weekdays: ['戟筠畇筠�逵', '極棘戟筠畇筠�逵克', '��棘�逵克', '��筠畇逵', '�筠�勻��逵克', '極筠�逵克', '��閨棘�逵'],
        weekdaysShort: ['戟筠畇.', '極棘戟.', '��棘.', '��筠.', '�筠�.', '極筠�.', '��閨.'],
        weekdaysMin: ['戟筠', '極棘', '��', '��', '�筠', '極筠', '��'],
        longDateFormat: {
            LT: 'H:mm',
            LTS : 'LT:ss',
            L: 'DD. MM. YYYY',
            LL: 'D. MMMM YYYY',
            LLL: 'D. MMMM YYYY LT',
            LLLL: 'dddd, D. MMMM YYYY LT'
        },
        calendar: {
            sameDay: '[畇逵戟逵� �] LT',
            nextDay: '[����逵 �] LT',

            nextWeek: function () {
                switch (this.day()) {
                case 0:
                    return '[�] [戟筠畇筠��] [�] LT';
                case 3:
                    return '[�] [��筠畇�] [�] LT';
                case 6:
                    return '[�] [��閨棘��] [�] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[�] dddd [�] LT';
                }
            },
            lastDay  : '[���筠 �] LT',
            lastWeek : function () {
                var lastWeekDays = [
                    '[極�棘�剋筠] [戟筠畇筠�筠] [�] LT',
                    '[極�棘�剋棘均] [極棘戟筠畇筠�克逵] [�] LT',
                    '[極�棘�剋棘均] [��棘�克逵] [�] LT',
                    '[極�棘�剋筠] [��筠畇筠] [�] LT',
                    '[極�棘�剋棘均] [�筠�勻��克逵] [�] LT',
                    '[極�棘�剋棘均] [極筠�克逵] [�] LT',
                    '[極�棘�剋筠] [��閨棘�筠] [�] LT'
                ];
                return lastWeekDays[this.day()];
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : '鈞逵 %s',
            past   : '極�筠 %s',
            s      : '戟筠克棘剋龜克棘 �筠克�戟畇龜',
            m      : translator.translate,
            mm     : translator.translate,
            h      : translator.translate,
            hh     : translator.translate,
            d      : '畇逵戟',
            dd     : translator.translate,
            M      : '劇筠�筠�',
            MM     : translator.translate,
            y      : '均棘畇龜戟�',
            yy     : translator.translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Serbian-latin (sr)
// author : Milan Jana훾kovi훶<milanjanackovic@gmail.com> : https://github.com/milan-j

(function (factory) {
    factory(moment);
}(function (moment) {
    var translator = {
        words: { //Different grammatical cases
            m: ['jedan minut', 'jedne minute'],
            mm: ['minut', 'minute', 'minuta'],
            h: ['jedan sat', 'jednog sata'],
            hh: ['sat', 'sata', 'sati'],
            dd: ['dan', 'dana', 'dana'],
            MM: ['mesec', 'meseca', 'meseci'],
            yy: ['godina', 'godine', 'godina']
        },
        correctGrammaticalCase: function (number, wordKey) {
            return number === 1 ? wordKey[0] : (number >= 2 && number <= 4 ? wordKey[1] : wordKey[2]);
        },
        translate: function (number, withoutSuffix, key) {
            var wordKey = translator.words[key];
            if (key.length === 1) {
                return withoutSuffix ? wordKey[0] : wordKey[1];
            } else {
                return number + ' ' + translator.correctGrammaticalCase(number, wordKey);
            }
        }
    };

    return moment.defineLocale('sr', {
        months: ['januar', 'februar', 'mart', 'april', 'maj', 'jun', 'jul', 'avgust', 'septembar', 'oktobar', 'novembar', 'decembar'],
        monthsShort: ['jan.', 'feb.', 'mar.', 'apr.', 'maj', 'jun', 'jul', 'avg.', 'sep.', 'okt.', 'nov.', 'dec.'],
        weekdays: ['nedelja', 'ponedeljak', 'utorak', 'sreda', '훾etvrtak', 'petak', 'subota'],
        weekdaysShort: ['ned.', 'pon.', 'uto.', 'sre.', '훾et.', 'pet.', 'sub.'],
        weekdaysMin: ['ne', 'po', 'ut', 'sr', '훾e', 'pe', 'su'],
        longDateFormat: {
            LT: 'H:mm',
            LTS : 'LT:ss',
            L: 'DD. MM. YYYY',
            LL: 'D. MMMM YYYY',
            LLL: 'D. MMMM YYYY LT',
            LLLL: 'dddd, D. MMMM YYYY LT'
        },
        calendar: {
            sameDay: '[danas u] LT',
            nextDay: '[sutra u] LT',

            nextWeek: function () {
                switch (this.day()) {
                case 0:
                    return '[u] [nedelju] [u] LT';
                case 3:
                    return '[u] [sredu] [u] LT';
                case 6:
                    return '[u] [subotu] [u] LT';
                case 1:
                case 2:
                case 4:
                case 5:
                    return '[u] dddd [u] LT';
                }
            },
            lastDay  : '[ju훾e u] LT',
            lastWeek : function () {
                var lastWeekDays = [
                    '[pro큄le] [nedelje] [u] LT',
                    '[pro큄log] [ponedeljka] [u] LT',
                    '[pro큄log] [utorka] [u] LT',
                    '[pro큄le] [srede] [u] LT',
                    '[pro큄log] [훾etvrtka] [u] LT',
                    '[pro큄log] [petka] [u] LT',
                    '[pro큄le] [subote] [u] LT'
                ];
                return lastWeekDays[this.day()];
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : 'za %s',
            past   : 'pre %s',
            s      : 'nekoliko sekundi',
            m      : translator.translate,
            mm     : translator.translate,
            h      : translator.translate,
            hh     : translator.translate,
            d      : 'dan',
            dd     : translator.translate,
            M      : 'mesec',
            MM     : translator.translate,
            y      : 'godinu',
            yy     : translator.translate
        },
        ordinalParse: /\d{1,2}\./,
        ordinal : '%d.',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : swedish (sv)
// author : Jens Alm : https://github.com/ulmus

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('sv', {
        months : 'januari_februari_mars_april_maj_juni_juli_augusti_september_oktober_november_december'.split('_'),
        monthsShort : 'jan_feb_mar_apr_maj_jun_jul_aug_sep_okt_nov_dec'.split('_'),
        weekdays : 's철ndag_m책ndag_tisdag_onsdag_torsdag_fredag_l철rdag'.split('_'),
        weekdaysShort : 's철n_m책n_tis_ons_tor_fre_l철r'.split('_'),
        weekdaysMin : 's철_m책_ti_on_to_fr_l철'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'YYYY-MM-DD',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[Idag] LT',
            nextDay: '[Imorgon] LT',
            lastDay: '[Ig책r] LT',
            nextWeek: 'dddd LT',
            lastWeek: '[F철rra] dddd[en] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'om %s',
            past : 'f철r %s sedan',
            s : 'n책gra sekunder',
            m : 'en minut',
            mm : '%d minuter',
            h : 'en timme',
            hh : '%d timmar',
            d : 'en dag',
            dd : '%d dagar',
            M : 'en m책nad',
            MM : '%d m책nader',
            y : 'ett 책r',
            yy : '%d 책r'
        },
        ordinalParse: /\d{1,2}(e|a)/,
        ordinal : function (number) {
            var b = number % 10,
                output = (~~(number % 100 / 10) === 1) ? 'e' :
                (b === 1) ? 'a' :
                (b === 2) ? 'a' :
                (b === 3) ? 'e' : 'e';
            return number + output;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : tamil (ta)
// author : Arjunkumar Krishnamoorthy : https://github.com/tk120404

(function (factory) {
    factory(moment);
}(function (moment) {
    /*var symbolMap = {
            '1': '晳�',
            '2': '晳�',
            '3': '晳�',
            '4': '晳�',
            '5': '晳�',
            '6': '晳�',
            '7': '晳�',
            '8': '晳�',
            '9': '晳�',
            '0': '晳�'
        },
        numberMap = {
            '晳�': '1',
            '晳�': '2',
            '晳�': '3',
            '晳�': '4',
            '晳�': '5',
            '晳�': '6',
            '晳�': '7',
            '晳�': '8',
            '晳�': '9',
            '晳�': '0'
        }; */

    return moment.defineLocale('ta', {
        months : '昔쒉�昔듀�昔�_昔む�昔む칾昔겯�昔겯�_昔��昔겯칾昔싟칾_昔뤲�晳띭�昔꿋칾_昔�칶_昔쒉칯昔⒯칾_昔쒉칯昔꿋칷_昔녱츜昔멘칾昔잀칾_昔싟칳昔む칾昔잀칳昔�칾昔む�晳�_昔끶츜晳띭츪晳뉋�昔む�晳�_昔ⓣ�昔�칾昔む�晳�_昔잀�昔싟�晳띭�昔겯칾'.split('_'),
        monthsShort : '昔쒉�昔듀�昔�_昔む�昔む칾昔겯�昔겯�_昔��昔겯칾昔싟칾_昔뤲�晳띭�昔꿋칾_昔�칶_昔쒉칯昔⒯칾_昔쒉칯昔꿋칷_昔녱츜昔멘칾昔잀칾_昔싟칳昔む칾昔잀칳昔�칾昔む�晳�_昔끶츜晳띭츪晳뉋�昔む�晳�_昔ⓣ�昔�칾昔む�晳�_昔잀�昔싟�晳띭�昔겯칾'.split('_'),
        weekdays : '昔왽�昔��昔긍칾昔긍칮昔뺖칾昔뺖�昔닮�晳�_昔ㅰ�昔쇸칾昔뺖츪晳띭츜昔욈�昔�칷_昔싟칳昔듀칾昔듀�昔�칾昔뺖�昔닮�晳�_昔む칮昔ㅰ�晳띭츜昔욈�昔�칷_昔듀�昔��昔닮츜晳띭츜昔욈�昔�칷_昔듀칳昔녀칾昔녀�昔뺖칾昔뺖�昔닮�晳�_昔싟�昔욈츜晳띭츜昔욈�昔�칷'.split('_'),
        weekdaysShort : '昔왽�昔��昔긍칮_昔ㅰ�昔쇸칾昔뺖�晳�_昔싟칳昔듀칾昔듀�昔�칾_昔む칮昔ㅰ�晳�_昔듀�昔��昔닮�晳�_昔듀칳昔녀칾昔녀�_昔싟�昔�'.split('_'),
        weekdaysMin : '昔왽�_昔ㅰ�_昔싟칳_昔む칮_昔듀�_昔듀칳_昔�'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY, LT',
            LLLL : 'dddd, D MMMM YYYY, LT'
        },
        calendar : {
            sameDay : '[昔뉋�晳띭�晳�] LT',
            nextDay : '[昔ⓣ�昔녀칷] LT',
            nextWeek : 'dddd, LT',
            lastDay : '[昔ⓣ칶昔긍칾昔긍칮] LT',
            lastWeek : '[昔뺖츪昔ⓣ칾昔� 昔듀�昔겯�晳�] dddd, LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s 昔뉋�晳�',
            past : '%s 昔�칮昔⒯칾',
            s : '昔믞�晳� 昔싟�昔� 昔듀�昔ⓣ�昔잀�昔뺖�晳�',
            m : '昔믞�晳� 昔ⓣ�昔��昔잀�晳�',
            mm : '%d 昔ⓣ�昔��昔잀츢晳띭츜昔녀칾',
            h : '昔믞�晳� 昔��昔� 昔ⓣ칶昔겯�晳�',
            hh : '%d 昔��昔� 昔ⓣ칶昔겯�晳�',
            d : '昔믞�晳� 昔ⓣ�昔녀칾',
            dd : '%d 昔ⓣ�昔잀칾昔뺖�晳�',
            M : '昔믞�晳� 昔��昔ㅰ�晳�',
            MM : '%d 昔��昔ㅰ츢晳띭츜昔녀칾',
            y : '昔믞�晳� 昔듀�晳곟츪昔�칾',
            yy : '%d 昔녱�晳띭츪晳곟츜昔녀칾'
        },
/*        preparse: function (string) {
            return string.replace(/[晳㏅�晳⒯�晳ム�晳��晳��]/g, function (match) {
                return numberMap[match];
            });
        },
        postformat: function (string) {
            return string.replace(/\d/g, function (match) {
                return symbolMap[match];
            });
        },*/
        ordinalParse: /\d{1,2}昔듀�晳�/,
        ordinal : function (number) {
            return number + '昔듀�晳�';
        },


        // refer http://ta.wikipedia.org/s/1er1
        meridiemParse: /昔��昔��晳�|昔듀칷昔뺖�晳�|昔뺖�昔꿋칷|昔ⓣ�晳띭�昔뺖�晳�|昔롞�晳띭�昔약츪晳�|昔��昔꿋칷/,
        meridiem : function (hour, minute, isLower) {
            if (hour < 2) {
                return ' 昔��昔��晳�';
            } else if (hour < 6) {
                return ' 昔듀칷昔뺖�晳�';  // 昔듀칷昔뺖�晳�
            } else if (hour < 10) {
                return ' 昔뺖�昔꿋칷'; // 昔뺖�昔꿋칷
            } else if (hour < 14) {
                return ' 昔ⓣ�晳띭�昔뺖�晳�'; // 昔ⓣ�晳띭�昔뺖�晳�
            } else if (hour < 18) {
                return ' 昔롞�晳띭�昔약츪晳�'; // 昔롞�晳띭�昔약츪晳�
            } else if (hour < 22) {
                return ' 昔��昔꿋칷'; // 昔��昔꿋칷
            } else {
                return ' 昔��昔��晳�';
            }
        },
        meridiemHour : function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === '昔��昔��晳�') {
                return hour < 2 ? hour : hour + 12;
            } else if (meridiem === '昔듀칷昔뺖�晳�' || meridiem === '昔뺖�昔꿋칷') {
                return hour;
            } else if (meridiem === '昔ⓣ�晳띭�昔뺖�晳�') {
                return hour >= 10 ? hour : hour + 12;
            } else {
                return hour + 12;
            }
        },
        week : {
            dow : 0, // Sunday is the first day of the week.
            doy : 6  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : thai (th)
// author : Kridsada Thanabulpong : https://github.com/sirn

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('th', {
        months : '錫□툈錫｀림錫꾝륫_錫곟만錫□툩錫꿋툧錫긍툢錫섁퉴_錫□링錫쇸림錫꾝륫_仙錫□릇錫꿋륭錫�_錫왽륵錫⒯툩錫꿋툌錫�_錫□릿錫뽤만錫쇸림錫№툢_錫곟르錫곟툗錫꿋툌錫�_錫む릿錫뉋릊錫꿋툌錫�_錫곟릴錫쇸륭錫꿋륭錫�_錫뺖만錫�림錫꾝륫_錫왽륵錫ⓣ툑錫닮툈錫꿋륭錫�_錫섁릴錫쇸름錫꿋툌錫�'.split('_'),
        monthsShort : '錫□툈錫｀림_錫곟만錫□툩錫�_錫□링錫쇸림_仙錫□릇錫�_錫왽륵錫⒯툩錫�_錫□릿錫뽤만錫쇸림_錫곟르錫곟툗錫�_錫む릿錫뉋릊錫�_錫곟릴錫쇸륭錫�_錫뺖만錫�림_錫왽륵錫ⓣ툑錫닮툈錫�_錫섁릴錫쇸름錫�'.split('_'),
        weekdays : '錫�림錫쀠릿錫뺖륭仙�_錫댽릴錫쇸툠錫｀퉴_錫�릴錫뉋툌錫꿋르_錫왽만錫�_錫왽륵錫ム릴錫む툣錫붲링_錫ⓣ만錫곟르仙�_仙錫む림錫｀퉴'.split('_'),
        weekdaysShort : '錫�림錫쀠릿錫뺖륭仙�_錫댽릴錫쇸툠錫｀퉴_錫�릴錫뉋툌錫꿋르_錫왽만錫�_錫왽륵錫ム릴錫�_錫ⓣ만錫곟르仙�_仙錫む림錫｀퉴'.split('_'), // yes, three characters difference
        weekdaysMin : '錫�림._錫�._錫�._錫�._錫왽륵._錫�._錫�.'.split('_'),
        longDateFormat : {
            LT : 'H 錫쇸림錫о릿錫곟림 m 錫쇸림錫쀠링',
            LTS : 'LT s 錫㏅릿錫쇸림錫쀠링',
            L : 'YYYY/MM/DD',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY 仙錫㏅른錫� LT',
            LLLL : '錫㏅릴錫셝ddd錫쀠링仙� D MMMM YYYY 仙錫㏅른錫� LT'
        },
        meridiemParse: /錫곟퉰錫�툢仙錫쀠링仙댽륭錫�|錫ム른錫긍툏仙錫쀠링仙댽륭錫�/,
        isPM: function (input) {
            return input === '錫ム른錫긍툏仙錫쀠링仙댽륭錫�';
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 12) {
                return '錫곟퉰錫�툢仙錫쀠링仙댽륭錫�';
            } else {
                return '錫ム른錫긍툏仙錫쀠링仙댽륭錫�';
            }
        },
        calendar : {
            sameDay : '[錫㏅릴錫쇸툢錫듀퉱 仙錫㏅른錫�] LT',
            nextDay : '[錫왽르錫멘퉰錫뉋툢錫듀퉱 仙錫㏅른錫�] LT',
            nextWeek : 'dddd[錫ム툢仙됢림 仙錫㏅른錫�] LT',
            lastDay : '[仙錫□막仙댽릎錫㏅림錫쇸툢錫듀퉱 仙錫㏅른錫�] LT',
            lastWeek : '[錫㏅릴錫�]dddd[錫쀠링仙댽퉩錫�퉱錫� 仙錫㏅른錫�] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '錫�링錫� %s',
            past : '%s錫쀠링仙댽퉩錫�퉱錫�',
            s : '仙꾝륫仙댽툈錫듀퉰錫㏅릿錫쇸림錫쀠링',
            m : '1 錫쇸림錫쀠링',
            mm : '%d 錫쇸림錫쀠링',
            h : '1 錫듺릴仙댽름仙귖륫錫�',
            hh : '%d 錫듺릴仙댽름仙귖륫錫�',
            d : '1 錫㏅릴錫�',
            dd : '%d 錫㏅릴錫�',
            M : '1 仙錫붲막錫�툢',
            MM : '%d 仙錫붲막錫�툢',
            y : '1 錫쎹링',
            yy : '%d 錫쎹링'
        }
    });
}));
// moment.js locale configuration
// locale : Tagalog/Filipino (tl-ph)
// author : Dan Hagman

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('tl-ph', {
        months : 'Enero_Pebrero_Marso_Abril_Mayo_Hunyo_Hulyo_Agosto_Setyembre_Oktubre_Nobyembre_Disyembre'.split('_'),
        monthsShort : 'Ene_Peb_Mar_Abr_May_Hun_Hul_Ago_Set_Okt_Nob_Dis'.split('_'),
        weekdays : 'Linggo_Lunes_Martes_Miyerkules_Huwebes_Biyernes_Sabado'.split('_'),
        weekdaysShort : 'Lin_Lun_Mar_Miy_Huw_Biy_Sab'.split('_'),
        weekdaysMin : 'Li_Lu_Ma_Mi_Hu_Bi_Sab'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'MM/D/YYYY',
            LL : 'MMMM D, YYYY',
            LLL : 'MMMM D, YYYY LT',
            LLLL : 'dddd, MMMM DD, YYYY LT'
        },
        calendar : {
            sameDay: '[Ngayon sa] LT',
            nextDay: '[Bukas sa] LT',
            nextWeek: 'dddd [sa] LT',
            lastDay: '[Kahapon sa] LT',
            lastWeek: 'dddd [huling linggo] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'sa loob ng %s',
            past : '%s ang nakalipas',
            s : 'ilang segundo',
            m : 'isang minuto',
            mm : '%d minuto',
            h : 'isang oras',
            hh : '%d oras',
            d : 'isang araw',
            dd : '%d araw',
            M : 'isang buwan',
            MM : '%d buwan',
            y : 'isang taon',
            yy : '%d taon'
        },
        ordinalParse: /\d{1,2}/,
        ordinal : function (number) {
            return number;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : turkish (tr)
// authors : Erhan Gundogan : https://github.com/erhangundogan,
//           Burak Yi휓it Kaya: https://github.com/BYK

(function (factory) {
    factory(moment);
}(function (moment) {
    var suffixes = {
        1: '\'inci',
        5: '\'inci',
        8: '\'inci',
        70: '\'inci',
        80: '\'inci',

        2: '\'nci',
        7: '\'nci',
        20: '\'nci',
        50: '\'nci',

        3: '\'체nc체',
        4: '\'체nc체',
        100: '\'체nc체',

        6: '\'nc캇',

        9: '\'uncu',
        10: '\'uncu',
        30: '\'uncu',

        60: '\'캇nc캇',
        90: '\'캇nc캇'
    };

    return moment.defineLocale('tr', {
        months : 'Ocak_힇ubat_Mart_Nisan_May캇s_Haziran_Temmuz_A휓ustos_Eyl체l_Ekim_Kas캇m_Aral캇k'.split('_'),
        monthsShort : 'Oca_힇ub_Mar_Nis_May_Haz_Tem_A휓u_Eyl_Eki_Kas_Ara'.split('_'),
        weekdays : 'Pazar_Pazartesi_Sal캇_횉ar힊amba_Per힊embe_Cuma_Cumartesi'.split('_'),
        weekdaysShort : 'Paz_Pts_Sal_횉ar_Per_Cum_Cts'.split('_'),
        weekdaysMin : 'Pz_Pt_Sa_횉a_Pe_Cu_Ct'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd, D MMMM YYYY LT'
        },
        calendar : {
            sameDay : '[bug체n saat] LT',
            nextDay : '[yar캇n saat] LT',
            nextWeek : '[haftaya] dddd [saat] LT',
            lastDay : '[d체n] LT',
            lastWeek : '[ge챌en hafta] dddd [saat] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s sonra',
            past : '%s 철nce',
            s : 'birka챌 saniye',
            m : 'bir dakika',
            mm : '%d dakika',
            h : 'bir saat',
            hh : '%d saat',
            d : 'bir g체n',
            dd : '%d g체n',
            M : 'bir ay',
            MM : '%d ay',
            y : 'bir y캇l',
            yy : '%d y캇l'
        },
        ordinalParse: /\d{1,2}'(inci|nci|체nc체|nc캇|uncu|캇nc캇)/,
        ordinal : function (number) {
            if (number === 0) {  // special case for zero
                return number + '\'캇nc캇';
            }
            var a = number % 10,
                b = number % 100 - a,
                c = number >= 100 ? 100 : null;

            return number + (suffixes[a] || suffixes[b] || suffixes[c]);
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Morocco Central Atlas Tamazit in Latin (tzm-latn)
// author : Abdel Said : https://github.com/abdelsaid

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('tzm-latn', {
        months : 'innayr_br胛ayr胛_mar胛s胛_ibrir_mayyw_ywnyw_ywlywz_w큄t_큄wtanbir_kt胛wbr胛_nwwanbir_dwjnbir'.split('_'),
        monthsShort : 'innayr_br胛ayr胛_mar胛s胛_ibrir_mayyw_ywnyw_ywlywz_w큄t_큄wtanbir_kt胛wbr胛_nwwanbir_dwjnbir'.split('_'),
        weekdays : 'asamas_aynas_asinas_akras_akwas_asimwas_asi搔뛹as'.split('_'),
        weekdaysShort : 'asamas_aynas_asinas_akras_akwas_asimwas_asi搔뛹as'.split('_'),
        weekdaysMin : 'asamas_aynas_asinas_akras_akwas_asimwas_asi搔뛹as'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[asdkh g] LT',
            nextDay: '[aska g] LT',
            nextWeek: 'dddd [g] LT',
            lastDay: '[assant g] LT',
            lastWeek: 'dddd [g] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : 'dadkh s yan %s',
            past : 'yan %s',
            s : 'imik',
            m : 'minu搔�',
            mm : '%d minu搔�',
            h : 'sa�a',
            hh : '%d tassa�in',
            d : 'ass',
            dd : '%d ossan',
            M : 'ayowr',
            MM : '%d iyyirn',
            y : 'asgas',
            yy : '%d isgasn'
        },
        week : {
            dow : 6, // Saturday is the first day of the week.
            doy : 12  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : Morocco Central Atlas Tamazit (tzm)
// author : Abdel Said : https://github.com/abdelsaid

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('tzm', {
        months : '竪됤탲竪뤴눗竪™탷_穗기탹穗겸덩竪�_竪롡눗竪뺚탾_竪됤눙竪붴탧竪�_竪롡눗竪™덩竪�_竪™탶竪뤴덩竪�_竪™탶竪띯덩竪볛덫_竪뽦탶竪쎻턀_竪쎻탶竪쒋눗竪뤴눙竪됤탷_穗썩턃竪볛눙竪�_竪뤴탶竪△눗竪뤴눙竪됤탷_穗룐탶竪듼탲穗기탧竪�'.split('_'),
        monthsShort : '竪됤탲竪뤴눗竪™탷_穗기탹穗겸덩竪�_竪롡눗竪뺚탾_竪됤눙竪붴탧竪�_竪롡눗竪™덩竪�_竪™탶竪뤴덩竪�_竪™탶竪띯덩竪볛덫_竪뽦탶竪쎻턀_竪쎻탶竪쒋눗竪뤴눙竪됤탷_穗썩턃竪볛눙竪�_竪뤴탶竪△눗竪뤴눙竪됤탷_穗룐탶竪듼탲穗기탧竪�'.split('_'),
        weekdays : '穗겸탽穗겸탮穗겸탽_穗겸덩竪뤴눗竪�_穗겸탽竪됤탲穗겸탽_穗겸늄竪붴눗竪�_穗겸늄竪△눗竪�_穗겸탽竪됤탮竪△눗竪�_穗겸탽竪됤뉩竪™눗竪�'.split('_'),
        weekdaysShort : '穗겸탽穗겸탮穗겸탽_穗겸덩竪뤴눗竪�_穗겸탽竪됤탲穗겸탽_穗겸늄竪붴눗竪�_穗겸늄竪△눗竪�_穗겸탽竪됤탮竪△눗竪�_穗겸탽竪됤뉩竪™눗竪�'.split('_'),
        weekdaysMin : '穗겸탽穗겸탮穗겸탽_穗겸덩竪뤴눗竪�_穗겸탽竪됤탲穗겸탽_穗겸늄竪붴눗竪�_穗겸늄竪△눗竪�_穗겸탽竪됤탮竪△눗竪�_穗겸탽竪됤뉩竪™눗竪�'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS: 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'dddd D MMMM YYYY LT'
        },
        calendar : {
            sameDay: '[穗겸탽穗룐탢 穗�] LT',
            nextDay: '[穗겸탽穗썩눗 穗�] LT',
            nextWeek: 'dddd [穗�] LT',
            lastDay: '[穗겸탾穗겸탲竪� 穗�] LT',
            lastWeek: 'dddd [穗�] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : '穗룐눗穗룐탢 竪� 竪™눗竪� %s',
            past : '竪™눗竪� %s',
            s : '竪됤탮竪됤늄',
            m : '竪롡탧竪뤴탶穗�',
            mm : '%d 竪롡탧竪뤴탶穗�',
            h : '竪쇺눗竪꾟눗',
            hh : '%d 竪쒋눗竪쇺탽穗겸탡竪됤탲',
            d : '穗겸탽竪�',
            dd : '%d o竪쇺탽穗겸탲',
            M : '穗겸덩o竪볛탷',
            MM : '%d 竪됤덩竪™탧竪붴탲',
            y : '穗겸탽穗년눗竪�',
            yy : '%d 竪됤탽穗년눗竪쇺탲'
        },
        week : {
            dow : 6, // Saturday is the first day of the week.
            doy : 12  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : ukrainian (uk)
// author : zemlanin : https://github.com/zemlanin
// Author : Menelion Elens첬le : https://github.com/Oire

(function (factory) {
    factory(moment);
}(function (moment) {
    function plural(word, num) {
        var forms = word.split('_');
        return num % 10 === 1 && num % 100 !== 11 ? forms[0] : (num % 10 >= 2 && num % 10 <= 4 && (num % 100 < 10 || num % 100 >= 20) ? forms[1] : forms[2]);
    }

    function relativeTimeWithPlural(number, withoutSuffix, key) {
        var format = {
            'mm': '�勻龜剋龜戟逵_�勻龜剋龜戟龜_�勻龜剋龜戟',
            'hh': '均棘畇龜戟逵_均棘畇龜戟龜_均棘畇龜戟',
            'dd': '畇筠戟�_畇戟�_畇戟�勻',
            'MM': '劇�����_劇�����_劇�����勻',
            'yy': '��克_�棘克龜_�棘克�勻'
        };
        if (key === 'm') {
            return withoutSuffix ? '�勻龜剋龜戟逵' : '�勻龜剋龜戟�';
        }
        else if (key === 'h') {
            return withoutSuffix ? '均棘畇龜戟逵' : '均棘畇龜戟�';
        }
        else {
            return number + ' ' + plural(format[key], +number);
        }
    }

    function monthsCaseReplace(m, format) {
        var months = {
            'nominative': '���筠戟�_剋��龜橘_閨筠�筠鈞筠戟�_克勻��筠戟�_��逵勻筠戟�_�筠�勻筠戟�_剋龜極筠戟�_�筠�極筠戟�_勻筠�筠�筠戟�_菌棘勻�筠戟�_剋龜��棘極逵畇_均��畇筠戟�'.split('_'),
            'accusative': '���戟�_剋��棘均棘_閨筠�筠鈞戟�_克勻��戟�_��逵勻戟�_�筠�勻戟�_剋龜極戟�_�筠�極戟�_勻筠�筠�戟�_菌棘勻�戟�_剋龜��棘極逵畇逵_均��畇戟�'.split('_')
        },

        nounCase = (/D[oD]? *MMMM?/).test(format) ?
            'accusative' :
            'nominative';

        return months[nounCase][m.month()];
    }

    function weekdaysCaseReplace(m, format) {
        var weekdays = {
            'nominative': '戟筠畇�剋�_極棘戟筠畇�剋棘克_勻�勻�棘�棘克_�筠�筠畇逵_�筠�勻筠�_極�쇥뤣궿싻먈녢�_��閨棘�逵'.split('_'),
            'accusative': '戟筠畇�剋�_極棘戟筠畇�剋棘克_勻�勻�棘�棘克_�筠�筠畇�_�筠�勻筠�_極�쇥뤣궿싻먈녢�_��閨棘��'.split('_'),
            'genitive': '戟筠畇�剋�_極棘戟筠畇�剋克逵_勻�勻�棘�克逵_�筠�筠畇龜_�筠�勻筠�均逵_極�쇥뤣궿싻먈녢�_��閨棘�龜'.split('_')
        },

        nounCase = (/(\[[�勻叫�]\]) ?dddd/).test(format) ?
            'accusative' :
            ((/\[?(?:劇龜戟�剋棘�|戟逵���極戟棘�)? ?\] ?dddd/).test(format) ?
                'genitive' :
                'nominative');

        return weekdays[nounCase][m.day()];
    }

    function processHoursFunction(str) {
        return function () {
            return str + '棘' + (this.hours() === 11 ? '閨' : '') + '] LT';
        };
    }

    return moment.defineLocale('uk', {
        months : monthsCaseReplace,
        monthsShort : '���_剋��_閨筠�_克勻��_��逵勻_�筠�勻_剋龜極_�筠�極_勻筠�_菌棘勻�_剋龜��_均��畇'.split('_'),
        weekdays : weekdaysCaseReplace,
        weekdaysShort : '戟畇_極戟_勻�_��_��_極�_�閨'.split('_'),
        weekdaysMin : '戟畇_極戟_勻�_��_��_極�_�閨'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD.MM.YYYY',
            LL : 'D MMMM YYYY �.',
            LLL : 'D MMMM YYYY �., LT',
            LLLL : 'dddd, D MMMM YYYY �., LT'
        },
        calendar : {
            sameDay: processHoursFunction('[鬼�棘均棘畇戟� '),
            nextDay: processHoursFunction('[�逵勻��逵 '),
            lastDay: processHoursFunction('[��棘�逵 '),
            nextWeek: processHoursFunction('[叫] dddd ['),
            lastWeek: function () {
                switch (this.day()) {
                case 0:
                case 3:
                case 5:
                case 6:
                    return processHoursFunction('[�龜戟�剋棘�] dddd [').call(this);
                case 1:
                case 2:
                case 4:
                    return processHoursFunction('[�龜戟�剋棘均棘] dddd [').call(this);
                }
            },
            sameElse: 'L'
        },
        relativeTime : {
            future : '鈞逵 %s',
            past : '%s �棘劇�',
            s : '畇筠克�剋�克逵 �筠克�戟畇',
            m : relativeTimeWithPlural,
            mm : relativeTimeWithPlural,
            h : '均棘畇龜戟�',
            hh : relativeTimeWithPlural,
            d : '畇筠戟�',
            dd : relativeTimeWithPlural,
            M : '劇�����',
            MM : relativeTimeWithPlural,
            y : '��克',
            yy : relativeTimeWithPlural
        },

        // M. E.: those two are virtually unused but a user might want to implement them for his/her website for some reason

        meridiemParse: /戟棘��|�逵戟克�|畇戟�|勻筠�棘�逵/,
        isPM: function (input) {
            return /^(畇戟�|勻筠�棘�逵)$/.test(input);
        },
        meridiem : function (hour, minute, isLower) {
            if (hour < 4) {
                return '戟棘��';
            } else if (hour < 12) {
                return '�逵戟克�';
            } else if (hour < 17) {
                return '畇戟�';
            } else {
                return '勻筠�棘�逵';
            }
        },

        ordinalParse: /\d{1,2}-(橘|均棘)/,
        ordinal: function (number, period) {
            switch (period) {
            case 'M':
            case 'd':
            case 'DDD':
            case 'w':
            case 'W':
                return number + '-橘';
            case 'D':
                return number + '-均棘';
            default:
                return number;
            }
        },

        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 1st is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : uzbek (uz)
// author : Sardor Muminov : https://github.com/muminoff

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('uz', {
        months : '�戟勻逵��_�筠勻�逵剋�_劇逵��_逵極�筠剋�_劇逵橘_龜�戟�_龜�剋�_逵勻均���_�筠戟��閨��_棘克��閨��_戟棘�閨��_畇筠克逵閨��'.split('_'),
        monthsShort : '�戟勻_�筠勻_劇逵�_逵極�_劇逵橘_龜�戟_龜�剋_逵勻均_�筠戟_棘克�_戟棘�_畇筠克'.split('_'),
        weekdays : '赳克�逵戟閨逵_���逵戟閨逵_鬼筠�逵戟閨逵_槻棘��逵戟閨逵_�逵橘�逵戟閨逵_��劇逵_珪逵戟閨逵'.split('_'),
        weekdaysShort : '赳克�_���_鬼筠�_槻棘�_�逵橘_��劇_珪逵戟'.split('_'),
        weekdaysMin : '赳克_��_鬼筠_槻棘_�逵_��_珪逵'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY LT',
            LLLL : 'D MMMM YYYY, dddd LT'
        },
        calendar : {
            sameDay : '[��均�戟 �棘逵�] LT [畇逵]',
            nextDay : '[葵��逵均逵] LT [畇逵]',
            nextWeek : 'dddd [克�戟龜 �棘逵�] LT [畇逵]',
            lastDay : '[�筠�逵 �棘逵�] LT [畇逵]',
            lastWeek : '[叫�均逵戟] dddd [克�戟龜 �棘逵�] LT [畇逵]',
            sameElse : 'L'
        },
        relativeTime : {
            future : '赳克龜戟 %s 龜�龜畇逵',
            past : '�龜� 戟筠�逵 %s 棘剋畇龜戟',
            s : '����逵�',
            m : '閨龜� 畇逵克龜克逵',
            mm : '%d 畇逵克龜克逵',
            h : '閨龜� �棘逵�',
            hh : '%d �棘逵�',
            d : '閨龜� 克�戟',
            dd : '%d 克�戟',
            M : '閨龜� 棘橘',
            MM : '%d 棘橘',
            y : '閨龜� 橘龜剋',
            yy : '%d 橘龜剋'
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 7  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : vietnamese (vi)
// author : Bang Nguyen : https://github.com/bangnk

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('vi', {
        months : 'th찼ng 1_th찼ng 2_th찼ng 3_th찼ng 4_th찼ng 5_th찼ng 6_th찼ng 7_th찼ng 8_th찼ng 9_th찼ng 10_th찼ng 11_th찼ng 12'.split('_'),
        monthsShort : 'Th01_Th02_Th03_Th04_Th05_Th06_Th07_Th08_Th09_Th10_Th11_Th12'.split('_'),
        weekdays : 'ch沼� nh梳춗_th沼� hai_th沼� ba_th沼� t튼_th沼� n훱m_th沼� s찼u_th沼� b梳즭'.split('_'),
        weekdaysShort : 'CN_T2_T3_T4_T5_T6_T7'.split('_'),
        weekdaysMin : 'CN_T2_T3_T4_T5_T6_T7'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'LT:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM [n훱m] YYYY',
            LLL : 'D MMMM [n훱m] YYYY LT',
            LLLL : 'dddd, D MMMM [n훱m] YYYY LT',
            l : 'DD/M/YYYY',
            ll : 'D MMM YYYY',
            lll : 'D MMM YYYY LT',
            llll : 'ddd, D MMM YYYY LT'
        },
        calendar : {
            sameDay: '[H척m nay l첬c] LT',
            nextDay: '[Ng횪y mai l첬c] LT',
            nextWeek: 'dddd [tu梳쬷 t沼쌻 l첬c] LT',
            lastDay: '[H척m qua l첬c] LT',
            lastWeek: 'dddd [tu梳쬷 r沼밿 l첬c] LT',
            sameElse: 'L'
        },
        relativeTime : {
            future : '%s t沼쌻',
            past : '%s tr튼沼쌵',
            s : 'v횪i gi창y',
            m : 'm沼셳 ph첬t',
            mm : '%d ph첬t',
            h : 'm沼셳 gi沼�',
            hh : '%d gi沼�',
            d : 'm沼셳 ng횪y',
            dd : '%d ng횪y',
            M : 'm沼셳 th찼ng',
            MM : '%d th찼ng',
            y : 'm沼셳 n훱m',
            yy : '%d n훱m'
        },
        ordinalParse: /\d{1,2}/,
        ordinal : function (number) {
            return number;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : chinese (zh-cn)
// author : suupic : https://github.com/suupic
// author : Zeno Zeng : https://github.com/zenozeng

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('zh-cn', {
        months : '訝��_雅뚧쐢_訝됪쐢_�쎿쐢_雅붹쐢_��쐢_訝껅쐢_�ユ쐢_阿앮쐢_�곫쐢_�곦���_�곦틠��'.split('_'),
        monthsShort : '1��_2��_3��_4��_5��_6��_7��_8��_9��_10��_11��_12��'.split('_'),
        weekdays : '�잍쐿��_�잍쐿訝_�잍쐿雅�_�잍쐿訝�_�잍쐿��_�잍쐿雅�_�잍쐿��'.split('_'),
        weekdaysShort : '�ⓩ뿥_�ⓧ�_�ⓧ틠_�ⓧ툒_�ⓨ썪_�ⓧ틪_�ⓨ뀷'.split('_'),
        weekdaysMin : '��_訝_雅�_訝�_��_雅�_��'.split('_'),
        longDateFormat : {
            LT : 'Ah�퉙m',
            LTS : 'Ah�퉙�냩燁�',
            L : 'YYYY-MM-DD',
            LL : 'YYYY亮퀾MMD��',
            LLL : 'YYYY亮퀾MMD�쩖T',
            LLLL : 'YYYY亮퀾MMD�쩮dddLT',
            l : 'YYYY-MM-DD',
            ll : 'YYYY亮퀾MMD��',
            lll : 'YYYY亮퀾MMD�쩖T',
            llll : 'YYYY亮퀾MMD�쩮dddLT'
        },
        meridiemParse: /�뚧솳|�⒳툓|訝듿뜄|訝�뜄|訝뗥뜄|�싦툓/,
        meridiemHour: function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === '�뚧솳' || meridiem === '�⒳툓' ||
                    meridiem === '訝듿뜄') {
                return hour;
            } else if (meridiem === '訝뗥뜄' || meridiem === '�싦툓') {
                return hour + 12;
            } else {
                // '訝�뜄'
                return hour >= 11 ? hour : hour + 12;
            }
        },
        meridiem : function (hour, minute, isLower) {
            var hm = hour * 100 + minute;
            if (hm < 600) {
                return '�뚧솳';
            } else if (hm < 900) {
                return '�⒳툓';
            } else if (hm < 1130) {
                return '訝듿뜄';
            } else if (hm < 1230) {
                return '訝�뜄';
            } else if (hm < 1800) {
                return '訝뗥뜄';
            } else {
                return '�싦툓';
            }
        },
        calendar : {
            sameDay : function () {
                return this.minutes() === 0 ? '[餓듿ㄹ]Ah[�방빐]' : '[餓듿ㄹ]LT';
            },
            nextDay : function () {
                return this.minutes() === 0 ? '[�롥ㄹ]Ah[�방빐]' : '[�롥ㄹ]LT';
            },
            lastDay : function () {
                return this.minutes() === 0 ? '[�ⓨㄹ]Ah[�방빐]' : '[�ⓨㄹ]LT';
            },
            nextWeek : function () {
                var startOfWeek, prefix;
                startOfWeek = moment().startOf('week');
                prefix = this.unix() - startOfWeek.unix() >= 7 * 24 * 3600 ? '[訝�]' : '[��]';
                return this.minutes() === 0 ? prefix + 'dddAh�방빐' : prefix + 'dddAh�퉙m';
            },
            lastWeek : function () {
                var startOfWeek, prefix;
                startOfWeek = moment().startOf('week');
                prefix = this.unix() < startOfWeek.unix()  ? '[訝�]' : '[��]';
                return this.minutes() === 0 ? prefix + 'dddAh�방빐' : prefix + 'dddAh�퉙m';
            },
            sameElse : 'LL'
        },
        ordinalParse: /\d{1,2}(��|��|��)/,
        ordinal : function (number, period) {
            switch (period) {
            case 'd':
            case 'D':
            case 'DDD':
                return number + '��';
            case 'M':
                return number + '��';
            case 'w':
            case 'W':
                return number + '��';
            default:
                return number;
            }
        },
        relativeTime : {
            future : '%s��',
            past : '%s��',
            s : '�좂쭜',
            m : '1�녽뮓',
            mm : '%d�녽뮓',
            h : '1弱뤸뿶',
            hh : '%d弱뤸뿶',
            d : '1鸚�',
            dd : '%d鸚�',
            M : '1訝ゆ쐢',
            MM : '%d訝ゆ쐢',
            y : '1亮�',
            yy : '%d亮�'
        },
        week : {
            // GB/T 7408-1994�딀빊��뀇�뚥벡�€졏凉뤒룝에��벡�◈룡뿥�잌뭽�띌뿴烏①ㅊ力뺛뗤툗ISO 8601:1988嶺됪븞
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));
// moment.js locale configuration
// locale : traditional chinese (zh-tw)
// author : Ben : https://github.com/ben-lin

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.defineLocale('zh-tw', {
        months : '訝��_雅뚧쐢_訝됪쐢_�쎿쐢_雅붹쐢_��쐢_訝껅쐢_�ユ쐢_阿앮쐢_�곫쐢_�곦���_�곦틠��'.split('_'),
        monthsShort : '1��_2��_3��_4��_5��_6��_7��_8��_9��_10��_11��_12��'.split('_'),
        weekdays : '�잍쐿��_�잍쐿訝_�잍쐿雅�_�잍쐿訝�_�잍쐿��_�잍쐿雅�_�잍쐿��'.split('_'),
        weekdaysShort : '�길뿥_�긴�_�긴틠_�긴툒_�긷썪_�긴틪_�긷뀷'.split('_'),
        weekdaysMin : '��_訝_雅�_訝�_��_雅�_��'.split('_'),
        longDateFormat : {
            LT : 'Ah容엕m',
            LTS : 'Ah容엕�냩燁�',
            L : 'YYYY亮퀾MMD��',
            LL : 'YYYY亮퀾MMD��',
            LLL : 'YYYY亮퀾MMD�쩖T',
            LLLL : 'YYYY亮퀾MMD�쩮dddLT',
            l : 'YYYY亮퀾MMD��',
            ll : 'YYYY亮퀾MMD��',
            lll : 'YYYY亮퀾MMD�쩖T',
            llll : 'YYYY亮퀾MMD�쩮dddLT'
        },
        meridiemParse: /�⒳툓|訝듿뜄|訝�뜄|訝뗥뜄|�싦툓/,
        meridiemHour : function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === '�⒳툓' || meridiem === '訝듿뜄') {
                return hour;
            } else if (meridiem === '訝�뜄') {
                return hour >= 11 ? hour : hour + 12;
            } else if (meridiem === '訝뗥뜄' || meridiem === '�싦툓') {
                return hour + 12;
            }
        },
        meridiem : function (hour, minute, isLower) {
            var hm = hour * 100 + minute;
            if (hm < 900) {
                return '�⒳툓';
            } else if (hm < 1130) {
                return '訝듿뜄';
            } else if (hm < 1230) {
                return '訝�뜄';
            } else if (hm < 1800) {
                return '訝뗥뜄';
            } else {
                return '�싦툓';
            }
        },
        calendar : {
            sameDay : '[餓듿ㄹ]LT',
            nextDay : '[�롥ㄹ]LT',
            nextWeek : '[訝�]ddddLT',
            lastDay : '[�ⓨㄹ]LT',
            lastWeek : '[訝�]ddddLT',
            sameElse : 'L'
        },
        ordinalParse: /\d{1,2}(��|��|��)/,
        ordinal : function (number, period) {
            switch (period) {
            case 'd' :
            case 'D' :
            case 'DDD' :
                return number + '��';
            case 'M' :
                return number + '��';
            case 'w' :
            case 'W' :
                return number + '��';
            default :
                return number;
            }
        },
        relativeTime : {
            future : '%s��',
            past : '%s��',
            s : '亮양쭜',
            m : '訝�녽릺',
            mm : '%d�녽릺',
            h : '訝弱뤸셽',
            hh : '%d弱뤸셽',
            d : '訝鸚�',
            dd : '%d鸚�',
            M : '訝�뗦쐢',
            MM : '%d�뗦쐢',
            y : '訝亮�',
            yy : '%d亮�'
        }
    });
}));

    moment.locale('en');


    /************************************
        Exposing Moment
    ************************************/

    function makeGlobal(shouldDeprecate) {
        /*global ender:false */
        if (typeof ender !== 'undefined') {
            return;
        }
        oldGlobalMoment = globalScope.moment;
        if (shouldDeprecate) {
            globalScope.moment = deprecate(
                    'Accessing Moment through the global scope is ' +
                    'deprecated, and will be removed in an upcoming ' +
                    'release.',
                    moment);
        } else {
            globalScope.moment = moment;
        }
    }

    // CommonJS module is defined
    if (hasModule) {
        module.exports = moment;
    } else if (typeof define === 'function' && define.amd) {
        define(function (require, exports, module) {
            if (module.config && module.config() && module.config().noGlobal === true) {
                // release the global variable
                globalScope.moment = oldGlobalMoment;
            }

            return moment;
        });
        makeGlobal(true);
    } else {
        makeGlobal();
    }
}).call(this);